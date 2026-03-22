package tn.iset.AppEvent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.iset.AppEvent.model.Role;
import tn.iset.AppEvent.model.User;
import tn.iset.AppEvent.repository.UserRepository;
import tn.iset.AppEvent.service.AuthService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        authService.initializeDefaultAdmin();
        initializeTestUsers();
        System.out.println("Default admin user initialized: admin@iset.tn / admin123");
        System.out.println("Test users initialized successfully");
    }
    
    private void initializeTestUsers() {
        if (!userRepository.existsByEmail("organisateur@test.tn")) {
            User organisateur = new User();
            organisateur.setFirstname("Ahmed");
            organisateur.setLastname("Ben Ali");
            organisateur.setEmail("organisateur@test.tn");
            organisateur.setPassword("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi");
            organisateur.setPhone("12345678");
            
            Set<Role> organisateurRoles = new HashSet<>();
            organisateurRoles.add(Role.ORGANISATEUR);
            organisateur.setRoles(organisateurRoles);
            
            userRepository.save(organisateur);
        }
        
        if (!userRepository.existsByEmail("participant@test.tn")) {
            User participant = new User();
            participant.setFirstname("Sarra");
            participant.setLastname("Mansour");
            participant.setEmail("participant@test.tn");
            participant.setPassword("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi");
            participant.setPhone("87654321");
            
            Set<Role> participantRoles = new HashSet<>();
            participantRoles.add(Role.PARTICIPANT);
            participant.setRoles(participantRoles);
            
            userRepository.save(participant);
        }
    }
}
