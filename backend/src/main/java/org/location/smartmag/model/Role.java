package org.location.smartmag.model; // Create this package: src/main/java/org/location/smartmag/model

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "ROLE_ADMIN", "ROLE_VENDEUR", "ROLE_MAGASINIER"

    // Constructor for creating a new role (without ID)
    public Role(String name) {
        this.name = name;
    }
}
