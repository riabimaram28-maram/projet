package tn.iset.AppEvent.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.iset.AppEvent.dto.LoginRequest;
import tn.iset.AppEvent.dto.LoginResponse;
import tn.iset.AppEvent.dto.LogoutResponse;
import tn.iset.AppEvent.dto.RegisterRequest;
import tn.iset.AppEvent.dto.RegisterResponse;
import tn.iset.AppEvent.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            RegisterResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working!");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Pour une implémentation JWT stateless, la déconnexion est gérée côté client
        // Le client doit simplement supprimer le token stocké
        LogoutResponse response = new LogoutResponse("Déconnexion réussie", true);
        return ResponseEntity.ok(response);
    }
}
