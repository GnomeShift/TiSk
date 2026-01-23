package com.gnomeshift.tisk.user;

import com.gnomeshift.tisk.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Get current user from session
    private User getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }

        log.warn("Current user not found");
        throw new AccessDeniedException("Authentication required");
    }

    private boolean isAdmin(User user) {
        return user.getRole() == UserRole.ADMIN;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Transactional()
    public UserDTO createUser(CreateUserDTO createUserDTO, Authentication authentication) {
        log.info("Creating new user with email: {}", createUserDTO.getEmail());

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        if (createUserDTO.getLogin() != null &&
                userRepository.existsByLogin(createUserDTO.getLogin())) {
            throw new ValidationException("Login already exists");
        }

        User userToCreate = userMapper.toEntity(createUserDTO);
        userToCreate.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        if (isAdmin(getCurrentUser(authentication))) {
            if (createUserDTO.getRole() != null) {
                userToCreate.setRole(createUserDTO.getRole());
            }
        }
        if (userToCreate.getLogin() == null) {
            userToCreate.setLogin(authService.generateLogin(createUserDTO.getFirstName(), createUserDTO.getLastName()));
        }

        User savedUser = userRepository.save(userToCreate);
        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Transactional()
    public UserDTO updateUser(UUID id, UpdateUserDTO updateUserDTO, Authentication authentication) {
        log.info("Updating user with id: {}", id);

        User currentUser = getCurrentUser(authentication);

        // User except admin can only edit yourself
        if (!isAdmin(currentUser) && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Access Denied");
        }

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updateUserDTO.getEmail() != null &&
                !updateUserDTO.getEmail().equals(userToUpdate.getEmail()) &&
                userRepository.existsByEmail(updateUserDTO.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        if (updateUserDTO.getLogin() != null && !updateUserDTO.getLogin().equals(userToUpdate.getLogin()) &&
                userRepository.existsByLogin(updateUserDTO.getLogin())) {
            throw new ValidationException("Login already exists");
        }

        userMapper.updateUserFromDto(updateUserDTO, userToUpdate);

        // If user isn't admin - ignore fields
        if (isAdmin(currentUser)) {
            if (updateUserDTO.getRole() != null) {
                userToUpdate.setRole(updateUserDTO.getRole());
            }
            if (updateUserDTO.getStatus() != null) {
                userToUpdate.setStatus(updateUserDTO.getStatus());
            }
        }

        log.info("User updated successfully: {}", id);
        return userMapper.toDto(userRepository.save(userToUpdate));
    }

    public void deleteUser(UUID id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    public void changeUserStatus(UUID id, UserStatus status) {
        log.info("Changing user status for id: {} to {}", id, status);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setStatus(status);
        userRepository.save(user);
        log.info("User status changed successfully: {}", id);
    }
}
