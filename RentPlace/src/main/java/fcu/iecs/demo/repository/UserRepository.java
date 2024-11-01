package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
 // List<User> findByFirstNameContaining(String keyword);

  //You can add custom queries here
}
