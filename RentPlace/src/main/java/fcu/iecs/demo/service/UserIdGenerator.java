package fcu.iecs.demo.service;
import fcu.iecs.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserIdGenerator {

  @Autowired
  private UserRepository userRepository;

  public String generateNewUserId() {
    // 找到現有最大的 ID
    Optional<String> maxId = userRepository.findMaxUserId();

    int nextId = 1;
    if (maxId.isPresent() && !maxId.get().isEmpty()) {
      // 去掉 "U" 並轉換為整數
      String currentMaxId = maxId.get().substring(1);
      nextId = Integer.parseInt(currentMaxId) + 1;
    }

    // 格式化成 U0001, U0002 等等
    return String.format("U%04d", nextId);
  }
}

