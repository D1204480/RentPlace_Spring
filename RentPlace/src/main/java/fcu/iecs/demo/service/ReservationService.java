package fcu.iecs.demo.service;



import fcu.iecs.demo.model.CloseDate;
import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.model.Payment;
import fcu.iecs.demo.model.Reservation;
import fcu.iecs.demo.repository.CloseDateRepository;
import fcu.iecs.demo.repository.PaymentRepository;
import fcu.iecs.demo.repository.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ReservationService {

//  @Autowired
//  private ReservationRepository reservationRepository;
//
//  public List<Reservation> getAllReservations() {
//    return reservationRepository.findAllWithTimePeriodAndStatus();
//  }
//
//  public Optional<Reservation> getReservationById(Integer id) {
//    return reservationRepository.findByIdWithTimePeriodAndStatus(id);
//  }
//
//  // 依照場地id查詢預約
//  public List<Reservation> getReservationsByVenueId(Integer venueId) {
//    return reservationRepository.findAllByVenueIdWithVenueAndStatus(venueId);
//  }
//
//  public Reservation createReservation(Reservation reservation) {
//    return reservationRepository.save(reservation);
//  }
//
//  // 更新預約
//  public Reservation updateReservation(Reservation reservation) {
//    // 檢查預約是否存在
//    if (!reservationRepository.existsById(reservation.getReservationId())) {
//      throw new RuntimeException("Reservation not found with id: " + reservation.getReservationId());
//    }
//    return reservationRepository.save(reservation);
//  }
//
//  // 刪除預約
//  public void deleteReservation(Integer id) {
//    // 檢查預約是否存在
//    if (!reservationRepository.existsById(id)) {
//      throw new RuntimeException("Reservation not found with id: " + id);
//    }
//    reservationRepository.deleteById(id);
//  }

  private final ReservationRepository reservationRepository;
  private final CloseDateRepository closeDateRepository;
  private final PaymentRepository paymentRepository;
  private final OrderService orderService;

  @Autowired
  public ReservationService(
      ReservationRepository reservationRepository,
      CloseDateRepository closeDateRepository,
      PaymentRepository paymentRepository,
      OrderService orderService
  ) {
    this.reservationRepository = reservationRepository;
    this.closeDateRepository = closeDateRepository;
    this.paymentRepository = paymentRepository;
    this.orderService = orderService;
  }


  @Transactional(readOnly = true)
  public List<Reservation> getAllReservations() {
    log.info("Getting all reservations");
    List<Reservation> reservations = reservationRepository.findAllWithTimePeriodAndStatus();
    // 預處理設備分類資訊
    reservations.forEach(reservation -> reservation.getEquipmentCategories());
    return reservations;
  }

  @Transactional(readOnly = true)
  public Optional<Reservation> getReservationById(Integer id) {
    log.info("Getting reservation by id: {}", id);
//    Optional<Reservation> reservation = reservationRepository.findByIdWithTimePeriodAndStatus(id);
    Optional<Reservation> reservation = reservationRepository.findById(id);
    // 如果找到預約，預處理設備分類資訊
    reservation.ifPresent(Reservation::getEquipmentCategories);
    return reservation;
  }

  @Transactional(readOnly = true)
  public List<Reservation> getReservationsByVenueId(Integer venueId) {
    log.info("Getting reservations by venue id: {}", venueId);
    List<Reservation> reservations = reservationRepository.findAllByVenueIdWithVenueAndStatus(venueId);

    // 取得場地的休館日
    List<CloseDate> closeDates = closeDateRepository.findByVenueId(venueId);

    // 將休館日資訊加入每個預約中
    reservations.forEach(reservation -> {
      if (reservation.getVenue() != null) {
        reservation.getVenue().setCloseDates(closeDates);
      }
      reservation.getEquipmentCategories();
    });

    return reservations;
  }

  @Transactional
//  public Reservation createReservation(Reservation reservation) {
//    log.info("Creating reservation for venue id: {}", reservation.getVenueId());
//    try {
//      Reservation savedReservation = reservationRepository.save(reservation);
//      // 預處理設備分類資訊
//      savedReservation.getEquipmentCategories();
//      return savedReservation;
//    } catch (Exception e) {
//      log.error("Error creating reservation", e);
//      throw new RuntimeException("Failed to create reservation", e);
//    }
//  }
  public Reservation createReservationWithOrder(Reservation reservation) {
    // 1. 設定初始狀態
    reservation.setStatusId(4);  // 資料庫status_id:5是待確認

    // 處理多時段，預約時段關聯（只在這裡處理一次）
    if (reservation.getTimePeriodIds() != null && !reservation.getTimePeriodIds().isEmpty()) {
      reservation.setTimePeriodIds(reservation.getTimePeriodIds());
    }

    // 儲存預訂
    Reservation savedReservation = reservationRepository.save(reservation);

    // 如果有設備ID，處理設備關聯
    if (reservation.getEquipmentIds() != null && !reservation.getEquipmentIds().isEmpty()) {
      reservation.setEquipmentIds(reservation.getEquipmentIds());
    }

    // 3. 創建付款記錄
    Payment payment = new Payment();
    payment.setPaymentMethod(reservation.getPaymentMethod());
    payment.setPaymentDate(LocalDateTime.now());
    // 只有在非線上支付且虛擬帳號不為空時才設定
    if (!"ONLINE_PAYMENT".equals(reservation.getPaymentMethod()) &&
        reservation.getVirtualAccount() != null &&
        !reservation.getVirtualAccount().trim().isEmpty()) {
      payment.setVirtualAccount(reservation.getVirtualAccount());
    }
    Payment savedPayment = paymentRepository.save(payment);

    // 4. 創建訂單
    Order order = new Order();
    order.setReservationId(savedReservation.getReservationId());
    order.setUserId(savedReservation.getUserId());
    order.setOrderDate(LocalDate.now());
    order.setStatusId(5);
    order.setPaymentId(savedPayment.getPaymentId());  // 關聯付款記錄
    order.setTotalAmount(reservation.getTotalAmount());  // 使用前端傳回的總金額
    orderService.createOrder(order);  // 使用 OrderService 來創建訂單

    return savedReservation;
  }

  @Transactional
  public Reservation updateReservation(Reservation reservation) {
    log.info("Updating reservation id: {}", reservation.getReservationId());
    if (!reservationRepository.existsById(reservation.getReservationId())) {
      throw new ResourceNotFoundException("Reservation not found with id: " + reservation.getReservationId());
    }
    try {
      Reservation updatedReservation = reservationRepository.save(reservation);
      // 預處理設備分類資訊
      updatedReservation.getEquipmentCategories();
      return updatedReservation;
    } catch (Exception e) {
      log.error("Error updating reservation", e);
      throw new RuntimeException("Failed to update reservation", e);
    }
  }

  @Transactional
  public void deleteReservation(Integer id) {
    log.info("Deleting reservation id: {}", id);
    if (!reservationRepository.existsById(id)) {
      throw new ResourceNotFoundException("Reservation not found with id: " + id);
    }
    try {
      reservationRepository.deleteById(id);
      log.info("Successfully deleted reservation id: {}", id);
    } catch (Exception e) {
      log.error("Error deleting reservation", e);
      throw new RuntimeException("Failed to delete reservation", e);
    }
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
      super(message);
    }
  }
}
