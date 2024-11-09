package fcu.iecs.demo.service;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.util.UserIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserIdGenerator userIdGenerator;

  @Transactional
  public User createUser(User user) {
    // 使用 UserIdGenerator 生成 ID
    String newUserId = userIdGenerator.generateNewUserId();
    user.setUserId(newUserId);

    // 保存用戶
    return userRepository.save(user);
  }

  public List<User> getAllUsers() {
    List<User> users = userRepository.findAll();
    System.out.println("Fetched Users: " + users);
    return users;
  }


  public Optional<User> getUserById(String userId) {
    return userRepository.findById(userId);
  }

//  public User createUser(User user) {
//    return userRepository.save(user);
//  }

  public User updateUser(String userId, User userDetails) {
    return userRepository.findById(userId)
        .map(user -> {
          user.setUsername(userDetails.getUsername());
          user.setPassword(userDetails.getPassword());
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
}