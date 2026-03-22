package tn.iset.AppEvent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tn.iset.AppEvent.model.Role;

public class RegisterRequest {
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 50)
    private String firstname;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50)
    private String lastname;
    
    @NotBlank(message = "L'email est obligatoire")
    @Size(max = 100)
    @Email(message = "Email invalide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Size(max = 120)
    private String password;
    
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmPassword;
    
    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String phone;
    
    private Role role;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String firstname, String lastname, String email, String password, String confirmPassword, String phone, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
        this.role = role;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
