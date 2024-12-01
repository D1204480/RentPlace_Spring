package fcu.iecs.demo.service;



import fcu.iecs.demo.model.Reservation;
import fcu.iecs.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;

  public List<Reservation> getAllReservations() {
    return reservationRepository.findAllWithTimePeriodAndStatus();
  }

  public Optional<Reservation> getReservationById(Integer id) {
    return reservationRepository.findByIdWithTimePeriodAndStatus(id);
  }

  // 依照場地id查詢預約
  public List<Reservation> getReservationsByVenueId(Integer venueId) {
    return reservationRepository.findAllByVenueIdWithVenueAndStatus(venueId);
  }

  public Reservation createReservation(Reservation reservation) {
    return reservationRepository.save(reservation);
  }

  // 更新預約
  public Reservation updateReservation(Reservation reservation) {
    // 檢查預約是否存在
    if (!reservationRepository.existsById(reservation.getReservationId())) {
      throw new RuntimeException("Reservation not found with id: " + reservation.getReservationId());
    }
    return reservationRepository.save(reservation);
  }

  // 刪除預約
  public void deleteReservation(Integer id) {
    // 檢查預約是否存在
    if (!reservationRepository.existsById(id)) {
      throw new RuntimeException("Reservation not found with id: " + id);
    }
    reservationRepository.deleteById(id);
  }

}
