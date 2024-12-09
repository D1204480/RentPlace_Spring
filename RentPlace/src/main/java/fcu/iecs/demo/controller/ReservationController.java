package fcu.iecs.demo.controller;



import fcu.iecs.demo.model.Reservation;
import fcu.iecs.demo.service.ReservationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@Slf4j
public class ReservationController {

//  @Autowired
//  private ReservationService reservationService;
//
//  @GetMapping
//  public ResponseEntity<List<Reservation>> getAllReservations() {
//    List<Reservation> reservations = reservationService.getAllReservations();
//    return ResponseEntity.ok(reservations);
//  }
//
//  @GetMapping("/{id}")
//  public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
//    Optional<Reservation> reservation = reservationService.getReservationById(id);
//    return reservation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//  }
//
//  @GetMapping("/venue/{venueId}")
//  public ResponseEntity<List<Reservation>> getReservationsByVenue(@PathVariable Integer venueId) {
//    List<Reservation> reservations = reservationService.getReservationsByVenueId(venueId);
//    return ResponseEntity.ok(reservations);
//  }
//
//  // 新增預約
//  @PostMapping
//  public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
//    Reservation savedReservation = reservationService.createReservation(reservation);
//    return ResponseEntity.ok(savedReservation);
//  }
//
//  // 更新預約
//  @PutMapping("/{id}")
//  public ResponseEntity<Reservation> updateReservation(
//      @PathVariable Integer id,
//      @RequestBody Reservation reservation) {
//    reservation.setReservationId(id);
//    Reservation updatedReservation = reservationService.updateReservation(reservation);
//    return ResponseEntity.ok(updatedReservation);
//  }
//
//  // 刪除預約
//  @DeleteMapping("/{id}")
//  public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
//    reservationService.deleteReservation(id);
//    return ResponseEntity.noContent().build();
//  }

  private final ReservationService reservationService;

  @Autowired
  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping
  public ResponseEntity<List<Reservation>> getAllReservations() {
    return ResponseEntity.ok(reservationService.getAllReservations());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
    return reservationService.getReservationById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/venue/{venueId}")
  public ResponseEntity<List<Reservation>> getReservationsByVenueId(@PathVariable Integer venueId) {
    return ResponseEntity.ok(reservationService.getReservationsByVenueId(venueId));
  }

  @PostMapping
//  public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
//    return ResponseEntity.ok(reservationService.createReservation(reservation));
//  }
  public ResponseEntity<Map<String, Object>> createReservation(@RequestBody Reservation reservation) {
    try {
      // 印出接收到的資料
      log.info("Received reservation data: {}", reservation);

      // 檢查必要欄位
      if (reservation.getVenueId() == null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "場地ID不能為空");
        return ResponseEntity.badRequest().body(errorResponse);
      }
      if (reservation.getUserId() == null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "使用者ID不能為空");
        return ResponseEntity.badRequest().body(errorResponse);
      }
      if (reservation.getTimePeriodId() == null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "時段ID不能為空");
        return ResponseEntity.badRequest().body(errorResponse);
      }
      if (reservation.getReservationDate() == null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "預約日期不能為空");
        return ResponseEntity.badRequest().body(errorResponse);
      }

      Reservation savedReservation = reservationService.createReservationWithOrder(reservation);

      Map<String, Object> response = new HashMap<>();
      response.put("reservationId", savedReservation.getReservationId());
      response.put("message", "預訂成功");

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      log.error("預訂創建失敗，接收到的資料: {}", reservation, e);
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "預訂創建失敗");
      errorResponse.put("message", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Reservation> updateReservation(
      @PathVariable Integer id,
      @RequestBody Reservation reservation) {
    reservation.setReservationId(id);
    return ResponseEntity.ok(reservationService.updateReservation(reservation));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
    reservationService.deleteReservation(id);
    return ResponseEntity.noContent().build();
  }
}
