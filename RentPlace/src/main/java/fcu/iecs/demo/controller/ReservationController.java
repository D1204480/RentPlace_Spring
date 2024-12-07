package fcu.iecs.demo.controller;



import fcu.iecs.demo.model.Reservation;
import fcu.iecs.demo.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
      Reservation savedReservation = reservationService.createReservationWithOrder(reservation);

      Map<String, Object> response = new HashMap<>();
      response.put("reservationId", savedReservation.getReservationId());
      response.put("message", "預訂成功");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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
