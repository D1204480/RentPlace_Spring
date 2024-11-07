package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

  @Query("SELECT MAX(u.userId) FROM User u")
  Optional<String> findMaxUserId();

}
