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
  @Query("SELECT MAX(CAST(SUBSTRING(u.userId, 2) AS int)) FROM User u")
  Optional<Integer> findMaxUserIdNumber();
  Optional<User> findByEmail(String email);
  Optional<User> findByUsername(String username);
  boolean existsByEmail(String email);
  boolean existsByUsername(String username);
}
