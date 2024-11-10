//package fcu.iecs.demo.service;
//import fcu.iecs.demo.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional(readOnly = true)  // 添加事務管理，只讀模式
//public class UserIdGenerator {
//
//  private final UserRepository userRepository;
//
//  // 使用構造函數注入
//  public UserIdGenerator(UserRepository userRepository) {
//    this.userRepository = userRepository;
//  }
//
//  public String generateNewUserId() {
//    // 找到現有最大的 ID
//    Optional<String> maxId = userRepository.findMaxUserId();
//
//    int nextId = 1;
//    if (maxId.isPresent() && !maxId.get().isEmpty()) {
//      // 去掉 "U" 並轉換為整數
//      String currentMaxId = maxId.get().substring(1);
//      nextId = Integer.parseInt(currentMaxId) + 1;
//    }
//
//    // 格式化成 U0001, U0002 等等
//    return String.format("U%04d", nextId);
//  }
//}
//
