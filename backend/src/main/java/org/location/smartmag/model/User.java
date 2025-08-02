package org.location.smartmag.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // Renamed from 'user' to 'users' to avoid potential SQL keyword conflicts
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // This will store the BCrypt encoded password

    @ManyToMany(fetch = FetchType.EAGER) // EAGERly load roles with user
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Constructor for creating a new user (without ID, as it's auto-generated)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
