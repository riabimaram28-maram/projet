package tn.iset.AppEvent.dto;

import jakarta.validation.constraints.NotBlank;
import tn.iset.AppEvent.model.Role;

public class ChangeRoleRequest {
    
    @NotBlank(message = "Le rôle est obligatoire")
    private Role role;
    
    private String reason;
    
    public ChangeRoleRequest() {}
    
    public ChangeRoleRequest(Role role, String reason) {
        this.role = role;
        this.reason = reason;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}
