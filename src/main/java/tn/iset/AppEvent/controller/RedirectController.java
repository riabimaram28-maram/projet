package tn.iset.AppEvent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/redirect")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RedirectController {
    
    private final UserRepository userRepository;
    
    public RedirectController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/after-login")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANISATEUR', 'PARTICIPANT')")
    public ResponseEntity<?> getRedirectPage(Authentication authentication) {
        Optional<User> userOpt = userRepository.findByEmail(authentication.getName());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        
        // Redirection selon le rôle
        if (user.getRoles().stream().anyMatch(role -> role.name().equals("ADMIN"))) {
            response.put("redirect", "/admin/dashboard");
            response.put("role", "ADMIN");
            response.put("message", "Redirection vers le tableau de bord administrateur");
        } else if (user.getRoles().stream().anyMatch(role -> role.name().equals("ORGANISATEUR"))) {
            response.put("redirect", "/organisateur/events");
            response.put("role", "ORGANISATEUR");
            response.put("message", "Redirection vers la gestion des événements");
        } else {
            response.put("redirect", "/participant/events");
            response.put("role", "PARTICIPANT");
            response.put("message", "Redirection vers la consultation des événements");
        }
        
        // Ajouter les informations utilisateur
        response.put("user", Map.of(
            "id", user.getId(),
            "firstname", user.getFirstname(),
            "lastname", user.getLastname(),
            "email", user.getEmail()
        ));
        
        return ResponseEntity.ok(response);
    }
}
