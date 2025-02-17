package fcu.iecs.demo.service;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.util.UserIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserIdGenerator userIdGenerator;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserIdGenerator userIdGenerator,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.userIdGenerator = userIdGenerator;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDetails loadUserByUserId(String userId) {
    User user = getUserById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        new ArrayList<>()
    );
  }

  public User createUser(User user) {
    // 在事務中生成 ID 和保存用戶
    String newUserId = userIdGenerator.generateNewUserId();
    user.setUserId(newUserId);

    // 加密密碼
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
    }
  }

    public List<User> getAllUsers() {
      return userRepository.findActiveUsers();
  }


  public Optional<User> getUserById(String userId) {
    return userRepository.findById(userId);
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

//  public User createUser(User user) {
//    return userRepository.save(user);
//  }

  public User updateUser(String userId, User userDetails) {
    return userRepository.findById(userId)
        .map(user -> {
          user.setUsername(userDetails.getUsername());
          // 如果更新密碼，也需要加密
          if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
          }
          user.setEmail(userDetails.getEmail());
          user.setPhone(userDetails.getPhone());
          user.setGender(userDetails.getGender());
          user.setBirth(userDetails.getBirth());
          return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  public void softDeleteUser(String userId) {
    User user = getUserById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    user.setStatusId(13);  // 13 表示用戶註銷狀態
    userRepository.save(user);
    System.out.println("用戶註銷成功: " + userId);
  }
}