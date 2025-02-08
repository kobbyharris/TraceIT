package com.vehicletrackingsys.api.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(length = 36, unique = true)
    private String id;

    private String roleName;

    @PrePersist
    private void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }


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
}
