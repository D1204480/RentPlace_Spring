package fcu.iecs.demo.controller;


import fcu.iecs.demo.model.Equipment;
import fcu.iecs.demo.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public Optional<Equipment> getEquipmentById(@PathVariable Integer id) {
        return equipmentService.getEquipmentById(id);
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Equipment>> getEquipmentByVenue(@PathVariable Integer venueId) {
        List<Equipment> equipment = equipmentService.getEquipmentByVenueId(venueId);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
    }


}

