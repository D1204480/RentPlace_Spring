package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.request.LoginRequest;
import fcu.iecs.demo.dto.request.RegisterRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.dto.response.JwtAuthenticationResponse;
import fcu.iecs.demo.dto.response.MessageResponse;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}) // 允許前端訪問
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
    this.authService = authService;
    this.jwtTokenProvider = jwtTokenProvider;
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

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    try {
      User user = authService.registerUser(registerRequest);
      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      JwtAuthenticationResponse response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }


}
