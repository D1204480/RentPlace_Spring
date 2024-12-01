package fcu.iecs.demo.service;



import fcu.iecs.demo.model.Equipment;
import fcu.iecs.demo.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Integer id) {
        return equipmentRepository.findById(id);
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public void deleteEquipment(Integer id) {
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> getEquipmentByVenueId(Integer venueId) {
        return equipmentRepository.findAllByVenueId(venueId);
    }
}
