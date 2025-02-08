package com.vehicletrackingsys.api.services;

import com.vehicletrackingsys.api.dtos.UserDTO;
import com.vehicletrackingsys.api.models.Role;
import com.vehicletrackingsys.api.models.User;
import com.vehicletrackingsys.api.models.Vehicle;
import com.vehicletrackingsys.api.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TrackingRepository trackingRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, VehicleRepository vehicleRepository,
                       DriverRepository driverRepository, TrackingRepository trackingRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.trackingRepository = trackingRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public UserDTO registerUser(UserDTO userDTO, String password) {
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> existingUsername = userRepository.findByUsername(userDTO.getUsername());

        if ((existingUser.isPresent() && existingUsername.isPresent())) {
            throw new IllegalStateException("User with this email already exists.");
        }

        // Create the new user object
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(password));

        // Save the user to generate the UUID
        User savedUser = userRepository.save(user);

        // SET USER ROLE
        Role role = new Role();
        role.setRoleName("USER");
        roleRepository.save(role);

        savedUser.setRole(role);  // Set the role of the user

        // Save the updated user with the role assigned
        userRepository.save(savedUser);

        // Return the registered user details (including role)
        UserDTO registeredUserDTO = new UserDTO();
        registeredUserDTO.setId(savedUser.getId());
        registeredUserDTO.setUsername(savedUser.getUsername());
        registeredUserDTO.setEmail(savedUser.getEmail());
        registeredUserDTO.setRoleName(role.getRoleName());
        return registeredUserDTO;
    }



    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDTO authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {

                Role role = user.getRole();

                // Map to UserDTO
                UserDTO authenticatedUser = new UserDTO();
                authenticatedUser.setId(user.getId());
                authenticatedUser.setUsername(user.getUsername());
                authenticatedUser.setEmail(user.getEmail());
                authenticatedUser.setRoleName(role != null ? role.getRoleName() : "USER"); // Default to "USER" if role is null
                return authenticatedUser;
            }
        }
        return null; // Invalid credentials
    }

    @Transactional
    public void updatePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(UUID userId, String username, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));

        user.setUsername(username);
        user.setEmail(email);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }



    public boolean userHasVehiclesByEmail(String email) {
        return !vehicleRepository.findByOwnerEmail(email).isEmpty();
    }
}
