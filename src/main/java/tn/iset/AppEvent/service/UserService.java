package tn.iset.AppEvent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.iset.AppEvent.dto.ChangeRoleRequest;
import tn.iset.AppEvent.dto.ResetPasswordRequest;
import tn.iset.AppEvent.dto.UpdateUserRequest;
import tn.iset.AppEvent.model.Role;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Map<String, Object> updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOpt.get();
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!user.getEmail().equals(updateUserRequest.getEmail()) && 
            userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new RuntimeException("Email déjà utilisé par un autre utilisateur");
        }
        
        // Mettre à jour les informations
        user.setFirstname(updateUserRequest.getFirstname());
        user.setLastname(updateUserRequest.getLastname());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhone(updateUserRequest.getPhone());
        
        // Mettre à jour le rôle si spécifié
        if (updateUserRequest.getRole() != null) {
            user.getRoles().clear();
            user.getRoles().add(updateUserRequest.getRole());
        }
        
        User updatedUser = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedUser.getId());
        response.put("firstname", updatedUser.getFirstname());
        response.put("lastname", updatedUser.getLastname());
        response.put("email", updatedUser.getEmail());
        response.put("phone", updatedUser.getPhone());
        response.put("roles", updatedUser.getRoles().stream()
                .map(role -> role.name())
                .collect(Collectors.toList()));
        response.put("updatedAt", updatedUser.getUpdatedAt());
        response.put("message", "Utilisateur mis à jour avec succès");
        
        return response;
    }
    
    public Map<String, Object> changeUserRole(Long userId, ChangeRoleRequest changeRoleRequest, String adminEmail) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOpt.get();
        
        // Empêcher la modification du rôle d'un administrateur par un autre admin
        if (user.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Impossible de modifier le rôle d'un administrateur");
        }
        
        // Mettre à jour le rôle
        user.getRoles().clear();
        user.getRoles().add(changeRoleRequest.getRole());
        
        User updatedUser = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", updatedUser.getId());
        response.put("email", updatedUser.getEmail());
        response.put("newRole", changeRoleRequest.getRole().name());
        response.put("changedBy", adminEmail);
        response.put("reason", changeRoleRequest.getReason());
        response.put("changedAt", LocalDateTime.now());
        response.put("message", "Rôle de l'utilisateur modifié avec succès");
        
        return response;
    }
    
    public Map<String, Object> resetUserPassword(ResetPasswordRequest resetPasswordRequest, String adminEmail) {
        Optional<User> userOpt = userRepository.findByEmail(resetPasswordRequest.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        if (!resetPasswordRequest.isPasswordMatching()) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }
        
        User user = userOpt.get();
        
        // Hasher le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        
        User updatedUser = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", updatedUser.getId());
        response.put("email", updatedUser.getEmail());
        response.put("resetBy", adminEmail);
        response.put("reason", resetPasswordRequest.getAdminReason());
        response.put("resetAt", LocalDateTime.now());
        response.put("message", "Mot de passe réinitialisé avec succès");
        
        return response;
    }
    
    public Map<String, Object> deleteUser(Long userId, String adminEmail) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOpt.get();
        
        // Empêcher la suppression d'un administrateur
        if (user.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Impossible de supprimer un administrateur");
        }
        
        String userEmail = user.getEmail();
        String userName = user.getFirstname() + " " + user.getLastname();
        
        userRepository.delete(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("deletedUserId", userId);
        response.put("deletedEmail", userEmail);
        response.put("deletedUserName", userName);
        response.put("deletedBy", adminEmail);
        response.put("deletedAt", LocalDateTime.now());
        response.put("message", "Utilisateur supprimé avec succès");
        
        return response;
    }
    
    public Map<String, Object> toggleUserStatus(Long userId, String adminEmail) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOpt.get();
        
        // Empêcher la désactivation d'un administrateur
        if (user.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Impossible de désactiver un administrateur");
        }
        
        // Pour cet exemple, on simule un statut actif/inactif avec un champ dans le nom
        // En pratique, vous devriez ajouter un champ "status" à l'entité User
        boolean isCurrentlyActive = !user.getFirstname().startsWith("[INACTIF] ");
        
        if (isCurrentlyActive) {
            user.setFirstname("[INACTIF] " + user.getFirstname());
        } else {
            user.setFirstname(user.getFirstname().replace("[INACTIF] ", ""));
        }
        
        User updatedUser = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", updatedUser.getId());
        response.put("email", updatedUser.getEmail());
        response.put("status", isCurrentlyActive ? "Désactivé" : "Activé");
        response.put("changedBy", adminEmail);
        response.put("changedAt", LocalDateTime.now());
        response.put("message", "Statut de l'utilisateur modifié avec succès");
        
        return response;
    }
    
    public Map<String, Object> getUserStatistics() {
        long totalUsers = userRepository.count();
        List<User> allUsers = userRepository.findAll();
        
        long adminCount = allUsers.stream()
                .filter(user -> user.getRoles().contains(Role.ADMIN))
                .count();
        
        long organisateurCount = allUsers.stream()
                .filter(user -> user.getRoles().contains(Role.ORGANISATEUR))
                .count();
        
        long participantCount = allUsers.stream()
                .filter(user -> user.getRoles().contains(Role.PARTICIPANT))
                .count();
        
        long inactiveCount = allUsers.stream()
                .filter(user -> user.getFirstname().startsWith("[INACTIF] "))
                .count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("adminCount", adminCount);
        stats.put("organisateurCount", organisateurCount);
        stats.put("participantCount", participantCount);
        stats.put("activeUsers", totalUsers - inactiveCount);
        stats.put("inactiveUsers", inactiveCount);
        stats.put("message", "Statistiques des utilisateurs");
        
        return stats;
    }
}
