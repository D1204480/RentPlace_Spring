// src/main/java/com/your/package/dto/request/GoogleLoginRequest.java
package fcu.iecs.demo.dto.request;

public class GoogleLoginRequest {
  private String googleToken;
  private String email;
  private String name;
  private String picture;

  // 構造函數
  public GoogleLoginRequest() {}

  // Getters and Setters
  public String getGoogleToken() {
    return googleToken;
  }

  public void setGoogleToken(String googleToken) {
    this.googleToken = googleToken;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}