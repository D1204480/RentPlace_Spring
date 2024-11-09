package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
 // List<User> findByFirstNameContaining(String keyword);

  //You can add custom queries here
  @Query("SELECT MAX(u.userId) FROM User u")
  String findMaxUserId();

  Optional<User> findByEmail(String email);
  Boolean existsByEmail(String email);

}
