package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
