package com.gnomeshift.tisk.auth;

import com.gnomeshift.tisk.user.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;

    public AuthResponseDTO register(RegisterDTO registerDTO) {
        log.info("Registering new user with email: {}", registerDTO.getEmail());

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        if (registerDTO.getLogin() != null && userRepository.existsByLogin(registerDTO.getLogin())) {
            throw new ValidationException("Login already taken");
        }

        User user = User.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .login(registerDTO.getLogin() != null ?
                        registerDTO.getLogin() : generateLogin(registerDTO.getFirstName(), registerDTO.getLastName()))
                .phoneNumber(registerDTO.getPhoneNumber())
                .department(registerDTO.getDepartment())
                .position(registerDTO.getPosition())
                .role(userRepository.count() == 0 ? UserRole.ADMIN : UserRole.USER)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        return buildAuthResponse(savedUser);
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        String userEmail = loginDTO.getEmail();

        log.info("Login attempt with email: {}", userEmail);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, loginDTO.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
        catch (DisabledException e) {
            throw new DisabledException("User account isn't active");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("User logged in successfully: {}", user.getId());
        return buildAuthResponse(user);
    }

    public AuthResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String dtoToken = refreshTokenDTO.getRefreshToken();
        String userEmail = jwtService.extractEmail(dtoToken);

        if (!jwtService.isRefreshToken(dtoToken)) {
            throw new BadCredentialsException("Token isn't a refresh token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        // Check status
        if (!user.isEnabled()) {
            throw new DisabledException("User account isn't active");
        }
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        // Token validation
        if (!jwtService.isTokenValid(dtoToken, user)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Token rotation
        log.info("Rotating tokens for user: {}", user.getId());
        return buildAuthResponse(user);
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpiration())
                .user(userMapper.toDto(user))
                .build();
    }

    public void changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user: {}", email);

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new ValidationException("Passwords don't match");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new ValidationException("Current password incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", user.getId());
    }

    public String generateLogin(String firstName, String lastName) {
        String base = (firstName.charAt(0) + lastName).toLowerCase().replaceAll("[^a-z0-9]", "");
        String login = base;
        int counter = 1;

        while (userRepository.existsByLogin(login)) {
            login = base + counter;
            counter++;
        }
        return login;
    }
}
