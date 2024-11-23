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

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Integer id) {
        reservationRepository.deleteById(id);
    }
}
