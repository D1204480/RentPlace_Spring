package fcu.iecs.demo.service;

import fcu.iecs.demo.model.Venue;
import fcu.iecs.demo.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(Integer id) {
        return venueRepository.findById(id);
    }

    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public Venue updateVenue(Integer id, Venue venueDetails) {
        return venueRepository.findById(id).map(venue -> {
            venue.setPlaceName(venueDetails.getPlaceName());
            venue.setVenueType(venueDetails.getVenueType());
            venue.setVenueName(venueDetails.getVenueName());
            venue.setRegionName(venueDetails.getRegionName());
            venue.setAddress(venueDetails.getAddress());
            venue.setUnitPrice(venueDetails.getUnitPrice());
            venue.setUnitType(venueDetails.getUnitType());
            venue.setCapacity(venueDetails.getCapacity());
            venue.setAvailableTime(venueDetails.getAvailableTime());
            venue.setRemark(venueDetails.getRemark());
            venue.setImageId(venueDetails.getImageId());
            return venueRepository.save(venue);
        }).orElseThrow(() -> new RuntimeException("Venue not found with id " + id));
    }

    public void deleteVenue(Integer id) {
        venueRepository.deleteById(id);
    }
}
