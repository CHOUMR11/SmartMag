package org.location.smartmag.controller; // Use your existing controller package

import org.location.smartmag.model.Role;
import org.location.smartmag.model.User;
import org.location.smartmag.repository.RoleRepository;
import org.location.smartmag.repository.UserRepository;
import org.location.smartmag.security.jwt.AuthRequest; // Create this DTO
import org.location.smartmag.security.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // If authentication is successful, generate JWT
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Encode password
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        // Assign default role (e.g., ROLE_VENDEUR) or specific roles from request
        Set<Role> roles = new HashSet<>();
        // Example: Assign a default role
        Role defaultRole = roleRepository.findByName("ROLE_VENDEUR")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        roles.add(defaultRole);
        registrationRequest.setRoles(roles);

        userRepository.save(registrationRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}
