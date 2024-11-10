// src/main/java/com/your/package/security/CustomUserDetailsService.java
package fcu.iecs.demo.security;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(String userId) {
    User user = userRepository.findById(userId)  // 這裡使用 findById
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

    return UserPrincipal.create(user);
  }
}