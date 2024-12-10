package fcu.iecs.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
  private String token;
  private String type;
  private String userId;
  private String username;
  private String email;
  private String phone;
}
