package fcu.iecs.demo.service;

import fcu.iecs.demo.model.CloseDate;
import fcu.iecs.demo.repository.CloseDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CloseDateService {
    @Autowired
    private CloseDateRepository closeDateRepository;

    public List<CloseDate> getCloseDatesByVenueId(Integer venueId) {
        return closeDateRepository.findByVenueIdWithVenueAndStatus(venueId);
    }

    public List<CloseDate> getAllCloseDates() {
        return closeDateRepository.findAllWithVenueAndStatus();
    }

    public Optional<CloseDate> getCloseDateById(Integer id) {
        return closeDateRepository.findById(id);
    }

    public CloseDate createCloseDate(CloseDate closeDate) {
        return closeDateRepository.save(closeDate);
    }

    public void deleteCloseDate(Integer id) {
        closeDateRepository.deleteById(id);
    }
}
