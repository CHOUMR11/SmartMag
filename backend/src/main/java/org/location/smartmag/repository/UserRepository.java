package org.location.smartmag.repository; // Create this package: src/main/java/org/location/smartmag/repository

import org.location.smartmag.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Spring Data JPA will automatically implement this
    Boolean existsByUsername(String username);
}
