package tn.iset.AppEvent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventController {
    
    private final UserRepository userRepository;
    
    public EventController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANISATEUR', 'PARTICIPANT')")
    public ResponseEntity<?> getDashboard(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("message", "Bienvenue sur votre tableau de bord");
        dashboard.put("user", Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "firstname", user.getFirstname(),
            "lastname", user.getLastname(),
            "roles", user.getRoles().stream()
                    .map(role -> role.name())
                    .toList()
        ));
        dashboard.put("features", getAvailableFeatures(user));
        
        return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminFeatures() {
        Map<String, Object> adminFeatures = new HashMap<>();
        adminFeatures.put("message", "Fonctionnalités administrateur");
        adminFeatures.put("features", java.util.List.of(
            "Gestion des utilisateurs",
            "Gestion des rôles",
            "Configuration système",
            "Rapports et statistiques",
            "Sauvegarde des données"
        ));
        
        return ResponseEntity.ok(adminFeatures);
    }
    
    @GetMapping("/organisateur")
    @PreAuthorize("hasRole('ORGANISATEUR')")
    public ResponseEntity<?> getOrganisateurFeatures() {
        Map<String, Object> organisateurFeatures = new HashMap<>();
        organisateurFeatures.put("message", "Fonctionnalités organisateur");
        organisateurFeatures.put("features", java.util.List.of(
            "Création d'événements",
            "Gestion des participants",
            "Modification des événements",
            "Statistiques des événements",
            "Export des données"
        ));
        
        return ResponseEntity.ok(organisateurFeatures);
    }
    
    @GetMapping("/participant")
    @PreAuthorize("hasRole('PARTICIPANT')")
    public ResponseEntity<?> getParticipantFeatures() {
        Map<String, Object> participantFeatures = new HashMap<>();
        participantFeatures.put("message", "Fonctionnalités participant");
        participantFeatures.put("features", java.util.List.of(
            "Consultation des événements",
            "Inscription aux événements",
            "Historique des participations",
            "Profil personnel",
            "Notifications"
        ));
        
        return ResponseEntity.ok(participantFeatures);
    }
    
    private java.util.List<String> getAvailableFeatures(User user) {
        return user.getRoles().stream()
                .flatMap(role -> switch (role) {
                    case ADMIN -> java.util.stream.Stream.of(
                        "Accès administrateur complet",
                        "Gestion des utilisateurs",
                        "Configuration système"
                    );
                    case ORGANISATEUR -> java.util.stream.Stream.of(
                        "Création d'événements",
                        "Gestion des participants",
                        "Statistiques"
                    );
                    case PARTICIPANT -> java.util.stream.Stream.of(
                        "Consultation des événements",
                        "Inscription aux événements",
                        "Profil personnel"
                    );
                })
                .distinct()
                .toList();
    }
}
