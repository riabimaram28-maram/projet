package tn.iset.AppEvent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.iset.AppEvent.dto.LoginRequest;
import tn.iset.AppEvent.dto.LoginResponse;
import tn.iset.AppEvent.dto.RegisterRequest;
import tn.iset.AppEvent.dto.RegisterResponse;
import tn.iset.AppEvent.model.Role;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;
import tn.iset.AppEvent.security.JwtTokenProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            String token = tokenProvider.generateToken(authentication);
            
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.name())
                    .collect(Collectors.toSet());
            
            return new LoginResponse(token, user.getId(), user.getEmail(), roles);
            
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password", e);
        }
    }
    
    public RegisterResponse register(RegisterRequest registerRequest) {
        // Validation de la confirmation du mot de passe
        if (!registerRequest.isPasswordMatching()) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        User user = new User();
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        
        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRole() != null) {
            roles.add(registerRequest.getRole());
        } else {
            roles.add(Role.PARTICIPANT);
        }
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        return new RegisterResponse(
            savedUser.getId(),
            savedUser.getFirstname(),
            savedUser.getLastname(),
            savedUser.getEmail(),
            savedUser.getPhone(),
            roles.iterator().next().name(),
            savedUser.getCreatedAt(),
            "Utilisateur inscrit avec succès"
        );
    }
    
    public void initializeDefaultAdmin() {
        if (!userRepository.existsByEmail("admin@iset.tn")) {
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("System");
            admin.setEmail("admin@iset.tn");
            admin.setPassword(passwordEncoder.encode("admin123"));
            
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.ADMIN);
            admin.setRoles(adminRoles);
            
            userRepository.save(admin);
        }
    }
}
