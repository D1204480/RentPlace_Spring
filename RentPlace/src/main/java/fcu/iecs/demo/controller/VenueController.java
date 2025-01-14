package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.VenueDTO;
import fcu.iecs.demo.model.Venue;
import fcu.iecs.demo.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequestMapping("/api/venues")
public class VenueController {

  private final VenueService venueService;

  public VenueController(VenueService venueService) {
    this.venueService = venueService;
  }

  @GetMapping
  public List<Venue> getAllVenues() {
    return venueService.getAllVenues();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Venue> getVenueById(@PathVariable Integer id) {
    try {
      Venue venue = venueService.getVenueById(id);
      return ResponseEntity.ok(venue);
    } catch (VenueService.VenueNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Venue> createVenue(@RequestBody VenueDTO venueDTO) {
    Venue venue = venueService.addVenue(venueDTO);
    return ResponseEntity.created(URI.create("/venues/" + venue.getId())).body(venue);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Venue> updateVenue(@PathVariable Integer id, @RequestBody VenueDTO venueDTO) {
    Venue venue = venueService.updateVenue(id, venueDTO);
    return ResponseEntity.ok(venue);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVenue(@PathVariable Integer id) {
    venueService.deleteVenue(id);
    return ResponseEntity.noContent().build();
  }
}