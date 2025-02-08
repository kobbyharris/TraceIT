package com.vehicletrackingsys.api.dtos;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Data
public class RoleDTO {
    private String id;
    private String roleName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters
}
