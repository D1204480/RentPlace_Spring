package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.request.LoginRequest;
import fcu.iecs.demo.dto.request.RegisterRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.dto.response.JwtAuthenticationResponse;
import fcu.iecs.demo.dto.response.MessageResponse;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.security.UserPrincipal;
import fcu.iecs.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}) // 允許前端訪問
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;


  public AuthController(AuthService authService) {
    this.authService = authService;

  }

  @PostMapping("/google-login")
  public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
    try {
      AuthResponse response = authService.handleGoogleLogin(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }


  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      log.info("Login attempt with email: {}", loginRequest.getEmail());
      JwtAuthenticationResponse response = authService.login(loginRequest);
      return ResponseEntity.ok(response);

    } catch (UsernameNotFoundException e) {
      log.warn("Login failed - user not found: {}", loginRequest.getEmail());
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(new MessageResponse(e.getMessage()));

    } catch (BadCredentialsException e) {
      log.warn("Login failed - bad credentials: {}", loginRequest.getEmail());
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new MessageResponse("Email或密碼錯誤"));

    } catch (Exception e) {
      log.error("Login error: ", e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new MessageResponse("登入過程發生錯誤"));
    }
  }


}
