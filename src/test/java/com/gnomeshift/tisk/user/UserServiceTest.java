package com.gnomeshift.tisk.user;

import com.gnomeshift.tisk.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("UserService Tests")
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;
    private UserDTO testUserDTO;
    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .login("testuser")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .department("IT")
                .position("Developer")
                .phoneNumber("+79001234567")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        adminUser = User.builder()
                .id(UUID.randomUUID())
                .email("admin@example.com")
                .password("encodedPassword")
                .firstName("Admin")
                .lastName("User")
                .login("adminuser")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testUserDTO = UserDTO.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .firstName(testUser.getFirstName())
                .lastName(testUser.getLastName())
                .login(testUser.getLogin())
                .role(testUser.getRole())
                .status(testUser.getStatus())
                .department(testUser.getDepartment())
                .position(testUser.getPosition())
                .build();

        createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("newuser@example.com");
        createUserDTO.setPassword("Password123");
        createUserDTO.setFirstName("New");
        createUserDTO.setLastName("User");
        createUserDTO.setLogin("newuser");
        createUserDTO.setRole(UserRole.USER);

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Updated");
        updateUserDTO.setLastName("User");
    }

    @Nested
    @DisplayName("Get all users Tests")
    class GetAllUsersTests {
        @Test
        @DisplayName("Return all users")
        void shouldReturnAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(testUser));
            when(userMapper.toDtoList(anyList())).thenReturn(List.of(testUserDTO));

            List<UserDTO> result = userService.getAllUsers();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Return empty list when no users")
        void shouldReturnEmptyListWhenNoUsers() {
            when(userRepository.findAll()).thenReturn(List.of());
            when(userMapper.toDtoList(anyList())).thenReturn(List.of());

            List<UserDTO> result = userService.getAllUsers();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Get user by ID Tests")
    class GetUserByIdTests {
        @Test
        @DisplayName("Return user by id")
        void shouldReturnUserById() {
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testUserDTO);

            UserDTO result = userService.getUserById(testUser.getId());

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            UUID randomId = UUID.randomUUID();
            when(userRepository.findById(randomId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(randomId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found with id");
        }
    }

    @Nested
    @DisplayName("Get user by Email Tests")
    class GetUserByEmailTests {
        @Test
        @DisplayName("Return user by email")
        void shouldReturnUserByEmail() {
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testUserDTO);

            UserDTO result = userService.getUserByEmail("test@example.com");

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Throw exception when user not found by email")
        void shouldThrowExceptionWhenUserNotFoundByEmail() {
            when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserByEmail("notfound@example.com"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found with email");
        }
    }

    @Nested
    @DisplayName("Create user Tests")
    class CreateUserTests {
        @Test
        @DisplayName("Create user successfully by admin")
        void shouldCreateUserByAdmin() {
            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByLogin(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(testUser);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            UserDTO result = userService.createUser(createUserDTO, authentication);

            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Admin can set user role")
        void shouldAllowAdminToSetRole() {
            createUserDTO.setRole(UserRole.SUPPORT);

            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByLogin(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(testUser);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                assertThat(saved.getRole()).isEqualTo(UserRole.SUPPORT);
                return saved;
            });
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            userService.createUser(createUserDTO, authentication);

            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Generate login when not provided")
        void shouldGenerateLoginWhenNotProvided() {
            createUserDTO.setLogin(null);
            User userWithoutLogin = User.builder()
                    .email(createUserDTO.getEmail())
                    .firstName(createUserDTO.getFirstName())
                    .lastName(createUserDTO.getLastName())
                    .login(null)
                    .build();

            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(userWithoutLogin);
            when(authService.generateLogin("New", "User")).thenReturn("nuser");
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            userService.createUser(createUserDTO, authentication);

            verify(authService).generateLogin("New", "User");
        }

        @Test
        @DisplayName("Throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(createUserDTO, authentication))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Email already exists");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Throw exception when login already exists")
        void shouldThrowExceptionWhenLoginExists() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByLogin(anyString())).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(createUserDTO, authentication))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Login already exists");

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Update user Tests")
    class UpdateUserTests {
        @Test
        @DisplayName("Update user successfully by self")
        void shouldUpdateUserBySelf() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            UserDTO result = userService.updateUser(testUser.getId(), updateUserDTO, authentication);

            assertThat(result).isNotNull();
            verify(userMapper).updateUserFromDto(updateUserDTO, testUser);
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("Update user successfully by admin")
        void shouldUpdateUserByAdmin() {
            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            UserDTO result = userService.updateUser(testUser.getId(), updateUserDTO, authentication);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Admin can update role and status")
        void shouldAllowAdminToUpdateRoleAndStatus() {
            updateUserDTO.setRole(UserRole.SUPPORT);
            updateUserDTO.setStatus(UserStatus.SUSPENDED);

            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                assertThat(saved.getRole()).isEqualTo(UserRole.SUPPORT);
                assertThat(saved.getStatus()).isEqualTo(UserStatus.SUSPENDED);
                return saved;
            });
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDTO);

            userService.updateUser(testUser.getId(), updateUserDTO, authentication);

            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Regular user cannot update other users")
        void shouldNotAllowRegularUserToUpdateOthers() {
            User otherUser = User.builder()
                    .id(UUID.randomUUID())
                    .role(UserRole.USER)
                    .build();

            when(authentication.getPrincipal()).thenReturn(otherUser);

            assertThatThrownBy(() -> userService.updateUser(testUser.getId(), updateUserDTO, authentication))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("Access Denied");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Throw exception when updating to existing email")
        void shouldThrowExceptionWhenUpdatingToExistingEmail() {
            updateUserDTO.setEmail("existing@example.com");

            when(authentication.getPrincipal()).thenReturn(testUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(testUser.getId(), updateUserDTO, authentication))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Email already exists");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Throw exception when updating to existing login")
        void shouldThrowExceptionWhenUpdatingToExistingLogin() {
            updateUserDTO.setLogin("existinglogin");

            when(authentication.getPrincipal()).thenReturn(testUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.existsByLogin("existinglogin")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(testUser.getId(), updateUserDTO, authentication))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Login already exists");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            when(authentication.getPrincipal()).thenReturn(adminUser);
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(UUID.randomUUID(), updateUserDTO, authentication))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete user Tests")
    class DeleteUserTests {
        @Test
        @DisplayName("Delete user successfully")
        void shouldDeleteUser() {
            when(userRepository.existsById(testUser.getId())).thenReturn(true);
            doNothing().when(userRepository).deleteById(testUser.getId());

            assertThatCode(() -> userService.deleteUser(testUser.getId()))
                    .doesNotThrowAnyException();

            verify(userRepository).deleteById(testUser.getId());
        }

        @Test
        @DisplayName("Throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(userRepository.existsById(nonExistentId)).thenReturn(false);

            assertThatThrownBy(() -> userService.deleteUser(nonExistentId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found with id");

            verify(userRepository, never()).deleteById(any(UUID.class));
        }
    }

    @Nested
    @DisplayName("Change user status Tests")
    class ChangeUserStatusTests {
        @Test
        @DisplayName("Change user status successfully")
        void shouldChangeUserStatus() {
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            assertThatCode(() -> userService.changeUserStatus(testUser.getId(), UserStatus.SUSPENDED))
                    .doesNotThrowAnyException();

            verify(userRepository).save(argThat(user -> user.getStatus() == UserStatus.SUSPENDED));
        }

        @Test
        @DisplayName("Throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.changeUserStatus(nonExistentId, UserStatus.ACTIVE))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found with id");

            verify(userRepository, never()).save(any(User.class));
        }
    }
}
