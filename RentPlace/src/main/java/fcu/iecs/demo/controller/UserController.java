package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  List<User> userList = new ArrayList<>();


  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{userId}")
  public User getUserById(@PathVariable String userId) {
    return userService.getUserById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }


  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @PutMapping("/{userId}")
  public User updateUser(@PathVariable String userId, @RequestBody User userDetails) {
    return userService.updateUser(userId, userDetails);
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
  }
}





