package fcu.iecs.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
  private String token;
  private String type = "Bearer";

  public JwtAuthenticationResponse(String token) {
    this.token = token;
  }
}
