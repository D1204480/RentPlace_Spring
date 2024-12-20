package fcu.iecs.demo.util;

import fcu.iecs.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


//@Component
//public class UserIdGenerator {
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Transactional
//  public String generateNewUserId() {
//    // 獲取最大的 user_id
//    String maxUserId = userRepository.findMaxUserId();
//
//    if (maxUserId == null) {
//      // 如果沒有用戶，從 U00001 開始
//      return "U00001";
//    }
//
//    // 解析數字部分
//    int currentNumber = Integer.parseInt(maxUserId.substring(1));
//    // 生成新的編號
//    String newNumber = String.format("U%05d", currentNumber + 1);
//
//    return newNumber;
//  }
//}

@Component
public class UserIdGenerator {
  private static final String USER_ID_PREFIX = "U";
  private static final String USER_ID_FORMAT = "U%05d";
  private static final int INITIAL_ID = 1;

  private final UserRepository userRepository;

  public UserIdGenerator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public synchronized String generateNewUserId() {
    try {
      // 獲取最大的數字部分
      int nextNumber = userRepository.findMaxUserIdNumber()
          .map(maxNumber -> maxNumber + 1)
          .orElse(INITIAL_ID);

      return String.format(USER_ID_FORMAT, nextNumber);
    } catch (Exception e) {
      throw new RuntimeException("Error generating user ID", e);
    }
  }
}