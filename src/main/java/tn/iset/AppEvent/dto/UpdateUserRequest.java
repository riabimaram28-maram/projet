package tn.iset.AppEvent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tn.iset.AppEvent.model.Role;

public class UpdateUserRequest {
    
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
    
    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String phone;
    
    private Role role;
    
    public UpdateUserRequest() {}
    
    public UpdateUserRequest(String firstname, String lastname, String email, String phone, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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
}
