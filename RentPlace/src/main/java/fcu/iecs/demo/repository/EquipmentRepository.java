package fcu.iecs.demo.repository;


import fcu.iecs.demo.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    // You can define custom query methods here if needed
}

