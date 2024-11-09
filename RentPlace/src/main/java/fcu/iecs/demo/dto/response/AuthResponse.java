// src/main/java/com/your/package/dto/response/AuthResponse.java
package fcu.iecs.demo.dto.response;

public class AuthResponse {
  private String accessToken;
  private UserDTO user;

  // 構造函數
  public AuthResponse(String accessToken, UserDTO user) {
    this.accessToken = accessToken;
    this.user = user;
  }

  // Getters and Setters
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }
}