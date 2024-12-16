// src/main/java/com/your/package/dto/response/UserDTO.java
package fcu.iecs.demo.dto;


import fcu.iecs.demo.model.User;
import lombok.Data;

import java.util.Date;


@Data
public class UserDTO {
  private String userId;
  private String username;
  private String email;
  private String phone;
  private String gender;
  private Date birth;

  public static UserDTO fromEntity(User user) {
    UserDTO dto = new UserDTO();
    dto.setUserId(user.getUserId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setPhone(user.getPhone());
    dto.setGender(user.getGender());
    dto.setBirth(user.getBirth());
    return dto;
  }
}