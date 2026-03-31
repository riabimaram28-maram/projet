package tn.iset.AppEvent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Size(max = 120)
    private String newPassword;
    
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmPassword;
    
    private String adminReason;
    
    public ResetPasswordRequest() {}
    
    public ResetPasswordRequest(String email, String newPassword, String confirmPassword, String adminReason) {
        this.email = email;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.adminReason = adminReason;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getAdminReason() {
        return adminReason;
    }
    
    public void setAdminReason(String adminReason) {
        this.adminReason = adminReason;
    }
    
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
