package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.CloseDate;
import fcu.iecs.demo.service.CloseDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequestMapping("/api/close-dates")
public class CloseDateController {

  @Autowired
  private CloseDateService closeDateService;

  @GetMapping("/venue/{venueId}")
  public ResponseEntity<List<CloseDate>> getCloseDatesByVenue(@PathVariable Integer venueId) {
    List<CloseDate> closeDates = closeDateService.getCloseDatesByVenueId(venueId);
    return ResponseEntity.ok(closeDates);
  }

  @GetMapping
  public ResponseEntity<List<CloseDate>> getAllCloseDates() {
    List<CloseDate> closeDates = closeDateService.getAllCloseDates();
    return ResponseEntity.ok(closeDates);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CloseDate> getCloseDateById(@PathVariable Integer id) {
    Optional<CloseDate> closeDate = closeDateService.getCloseDateById(id);
    return closeDate.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<CloseDate> createCloseDate(@RequestBody CloseDate closeDate) {
    CloseDate savedCloseDate = closeDateService.createCloseDate(closeDate);
    return ResponseEntity.ok(savedCloseDate);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCloseDate(@PathVariable Integer id) {
    closeDateService.deleteCloseDate(id);
    return ResponseEntity.noContent().build();
  }
}
