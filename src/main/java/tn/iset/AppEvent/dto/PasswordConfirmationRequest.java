package tn.iset.AppEvent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordConfirmationRequest {
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Size(max = 120)
    private String password;
    
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmPassword;
    
    public PasswordConfirmationRequest() {}
    
    public PasswordConfirmationRequest(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
