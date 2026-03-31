package tn.iset.AppEvent.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.iset.AppEvent.dto.ChangeRoleRequest;
import tn.iset.AppEvent.dto.ResetPasswordRequest;
import tn.iset.AppEvent.dto.UpdateUserRequest;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;
import tn.iset.AppEvent.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    private final UserRepository userRepository;
    private final UserService userService;
    
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
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
    
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserStatistics() {
        return ResponseEntity.ok(userService.getUserStatistics());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @Valid @RequestBody ChangeRoleRequest changeRoleRequest, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.changeUserRole(id, changeRoleRequest, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetUserPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.resetUserPassword(resetPasswordRequest, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id, Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.toggleUserStatus(id, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
