// src/main/java/com/your/package/security/UserPrincipal.java
package fcu.iecs.demo.security;

import fcu.iecs.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
  private String userId;     // 改為 String
  private String email;
  private String username;
  private Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(String userId, String email, String username, Collection<? extends GrantedAuthority> authorities) {
    this.userId = userId;
    this.email = email;
    this.username = username;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
    List<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_USER")
    );

    return new UserPrincipal(
        user.getUserId(),
        user.getEmail(),
        user.getUsername(),
        authorities
    );
  }

  public String getUserId() {
    return userId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return null; // Google 登入不需要密碼
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}