package tn.iset.AppEvent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    private final UserRepository userRepository;
    
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANISATEUR', 'PARTICIPANT')")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        Optional<User> userOpt = userRepository.findByEmail(authentication.getName());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("firstname", user.getFirstname());
        profile.put("lastname", user.getLastname());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());
        profile.put("roles", user.getRoles().stream()
                .map(role -> role.name())
                .toList());
        profile.put("createdAt", user.getCreatedAt());
        
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        List<Map<String, Object>> userProfiles = users.stream()
                .map(user -> {
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("id", user.getId());
                    profile.put("firstname", user.getFirstname());
                    profile.put("lastname", user.getLastname());
                    profile.put("email", user.getEmail());
                    profile.put("phone", user.getPhone());
                    profile.put("roles", user.getRoles().stream()
                            .map(role -> role.name())
                            .toList());
                    profile.put("createdAt", user.getCreatedAt());
                    return profile;
                })
                .toList();
        
        return ResponseEntity.ok(userProfiles);
    }
    
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserCount() {
        long totalUsers = userRepository.count();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("message", "Statistiques des utilisateurs");
        
        return ResponseEntity.ok(stats);
    }
}
