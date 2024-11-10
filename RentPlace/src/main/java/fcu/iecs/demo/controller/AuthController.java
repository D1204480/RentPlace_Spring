package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}) // 允許前端訪問
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/google-login")
  public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
    try {
      AuthResponse response = authService.handleGoogleLogin(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
