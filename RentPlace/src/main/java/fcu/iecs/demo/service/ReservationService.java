package fcu.iecs.demo.service;



import fcu.iecs.demo.model.Reservation;
import fcu.iecs.demo.repository.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

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

  @Autowired
  public ReservationService(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
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
    Optional<Reservation> reservation = reservationRepository.findByIdWithTimePeriodAndStatus(id);
    // 如果找到預約，預處理設備分類資訊
    reservation.ifPresent(r -> r.getEquipmentCategories());
    return reservation;
  }

  @Transactional(readOnly = true)
  public List<Reservation> getReservationsByVenueId(Integer venueId) {
    log.info("Getting reservations by venue id: {}", venueId);
    List<Reservation> reservations = reservationRepository.findAllByVenueIdWithVenueAndStatus(venueId);
    // 預處理設備分類資訊
    reservations.forEach(reservation -> reservation.getEquipmentCategories());
    return reservations;
  }

  @Transactional
  public Reservation createReservation(Reservation reservation) {
    log.info("Creating reservation for venue id: {}", reservation.getVenueId());
    try {
      Reservation savedReservation = reservationRepository.save(reservation);
      // 預處理設備分類資訊
      savedReservation.getEquipmentCategories();
      return savedReservation;
    } catch (Exception e) {
      log.error("Error creating reservation", e);
      throw new RuntimeException("Failed to create reservation", e);
    }
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
