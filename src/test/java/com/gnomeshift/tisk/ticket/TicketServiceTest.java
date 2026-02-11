package com.gnomeshift.tisk.ticket;

import com.gnomeshift.tisk.security.HtmlSanitizer;
import com.gnomeshift.tisk.user.User;
import com.gnomeshift.tisk.user.UserRepository;
import com.gnomeshift.tisk.user.UserRole;
import com.gnomeshift.tisk.user.UserStatus;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("TicketService Tests")
class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private HtmlSanitizer htmlSanitizer;

    private Ticket testTicket;
    private TicketDTO testTicketDTO;
    private User testUser;
    private User testAdmin;
    private User testAssignee;
    private CreateTicketDTO createTicketDTO;
    private UpdateTicketDTO updateTicketDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .login("testuser")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testAdmin = User.builder()
                .id(UUID.randomUUID())
                .email("admin@example.com")
                .password("password")
                .firstName("Admin")
                .lastName("User")
                .login("adminuser")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testAssignee = User.builder()
                .id(UUID.randomUUID())
                .email("support@example.com")
                .password("password")
                .firstName("Support")
                .lastName("User")
                .login("supportuser")
                .role(UserRole.SUPPORT)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testTicket = Ticket.builder()
                .id(UUID.randomUUID())
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.MEDIUM)
                .reporter(testUser)
                .assignee(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testTicketDTO = TicketDTO.builder()
                .id(testTicket.getId())
                .title(testTicket.getTitle())
                .description(testTicket.getDescription())
                .status(testTicket.getStatus())
                .priority(testTicket.getPriority())
                .createdAt(testTicket.getCreatedAt())
                .updatedAt(testTicket.getUpdatedAt())
                .build();

        createTicketDTO = new CreateTicketDTO();
        createTicketDTO.setTitle("New Ticket");
        createTicketDTO.setDescription("New Description");
        createTicketDTO.setPriority(TicketPriority.HIGH);
        createTicketDTO.setReporterId(testUser.getId());

        updateTicketDTO = new UpdateTicketDTO();
        updateTicketDTO.setTitle("Updated Title");
    }

    @Nested
    @DisplayName("Get all tickets Tests")
    class GetAllTicketsTests {
        @Test
        @DisplayName("Return all tickets")
        void shouldReturnAllTickets() {
            when(ticketRepository.findAll()).thenReturn(List.of(testTicket));
            when(ticketMapper.toDtoList(anyList())).thenReturn(List.of(testTicketDTO));

            List<TicketDTO> result = ticketService.getAllTickets();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().getTitle()).isEqualTo("Test Ticket");
        }

        @Test
        @DisplayName("Return empty list when no tickets")
        void shouldReturnEmptyListWhenNoTickets() {
            when(ticketRepository.findAll()).thenReturn(List.of());
            when(ticketMapper.toDtoList(anyList())).thenReturn(List.of());

            List<TicketDTO> result = ticketService.getAllTickets();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Get ticket by ID Tests")
    class GetTicketByIdTests {
        @Test
        @DisplayName("Return ticket by id when user is reporter")
        void shouldReturnTicketByIdWhenUserIsReporter() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.getTicketById(testTicket.getId(), authentication);

            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("Test Ticket");
        }

        @Test
        @DisplayName("Return ticket by id when user is staff")
        void shouldReturnTicketByIdWhenUserIsStaff() {
            when(authentication.getPrincipal()).thenReturn(testAdmin);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.getTicketById(testTicket.getId(), authentication);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Return ticket by id when user is assignee")
        void shouldReturnTicketByIdWhenUserIsAssignee() {
            testTicket.setAssignee(testAssignee);
            User assigneeAsUser = User.builder()
                    .id(testAssignee.getId())
                    .role(UserRole.USER)
                    .build();

            when(authentication.getPrincipal()).thenReturn(assigneeAsUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.getTicketById(testTicket.getId(), authentication);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Throw AccessDeniedException when user is not owner/assignee/staff")
        void shouldThrowAccessDenied() {
            User otherUser = User.builder().id(UUID.randomUUID()).role(UserRole.USER).build();
            when(authentication.getPrincipal()).thenReturn(otherUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));

            assertThatThrownBy(() -> ticketService.getTicketById(testTicket.getId(), authentication))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("Access Denied");
        }

        @Test
        @DisplayName("Throw exception when ticket not found")
        void shouldThrowExceptionWhenTicketNotFound() {
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.getTicketById(UUID.randomUUID(), authentication))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Ticket not found");
        }
    }

    @Nested
    @DisplayName("Get my tickets Tests")
    class GetMyTicketsTests {
        @Test
        @DisplayName("Return tickets for current user")
        void shouldReturnTicketsForCurrentUser() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketRepository.findAllByReporter(testUser)).thenReturn(List.of(testTicket));
            when(ticketMapper.toDtoList(anyList())).thenReturn(List.of(testTicketDTO));

            List<TicketDTO> result = ticketService.getMyTickets(authentication);

            assertThat(result).hasSize(1);
            verify(ticketRepository).findAllByReporter(testUser);
        }

        @Test
        @DisplayName("Return empty list when user has no tickets")
        void shouldReturnEmptyListWhenUserHasNoTickets() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketRepository.findAllByReporter(testUser)).thenReturn(List.of());
            when(ticketMapper.toDtoList(anyList())).thenReturn(List.of());

            List<TicketDTO> result = ticketService.getMyTickets(authentication);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Throw exception when authentication principal is not User")
        void shouldThrowExceptionWhenPrincipalIsNotUser() {
            when(authentication.getPrincipal()).thenReturn("not-a-user-object");

            assertThatThrownBy(() -> ticketService.getMyTickets(authentication))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("User not found");
        }
    }

    @Nested
    @DisplayName("Create ticket Tests")
    class CreateTicketTests {
        @Test
        @DisplayName("Create ticket successfully for regular user")
        void shouldCreateTicketForRegularUser() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(userRepository.getReferenceById(testUser.getId())).thenReturn(testUser);
            when(ticketMapper.toEntity(any(CreateTicketDTO.class))).thenReturn(testTicket);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);
            when(htmlSanitizer.sanitize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

            TicketDTO result = ticketService.createTicket(createTicketDTO, authentication);

            assertThat(result).isNotNull();
            verify(ticketRepository).save(any(Ticket.class));
            verify(userRepository).getReferenceById(testUser.getId());
        }

        @Test
        @DisplayName("Staff can create ticket for another user")
        void shouldAllowStaffToCreateTicketForAnotherUser() {
            UUID otherUserId = UUID.randomUUID();
            User otherUser = User.builder().id(otherUserId).build();
            createTicketDTO.setReporterId(otherUserId);

            when(authentication.getPrincipal()).thenReturn(testAdmin);
            when(userRepository.findById(otherUserId)).thenReturn(Optional.of(otherUser));
            when(ticketMapper.toEntity(any(CreateTicketDTO.class))).thenReturn(testTicket);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);
            when(htmlSanitizer.sanitize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

            ticketService.createTicket(createTicketDTO, authentication);

            verify(userRepository).findById(otherUserId);
        }

        @Test
        @DisplayName("User cannot create ticket for another user")
        void shouldNotAllowRegularUserToCreateTicketForAnother() {
            UUID otherUserId = UUID.randomUUID();
            createTicketDTO.setReporterId(otherUserId);

            when(authentication.getPrincipal()).thenReturn(testUser);
            when(userRepository.getReferenceById(testUser.getId())).thenReturn(testUser);
            when(ticketMapper.toEntity(any(CreateTicketDTO.class))).thenReturn(testTicket);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);
            when(htmlSanitizer.sanitize(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

            ticketService.createTicket(createTicketDTO, authentication);

            verify(ticketRepository).save(any(Ticket.class));
        }
    }

    @Nested
    @DisplayName("Update ticket Tests")
    class UpdateTicketTests {
        @Test
        @DisplayName("Update ticket successfully by reporter")
        void shouldUpdateTicketByReporter() {
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.updateTicket(testTicket.getId(), updateTicketDTO, authentication);

            assertThat(result).isNotNull();
            verify(ticketMapper).updateTicketFromDto(any(UpdateTicketDTO.class), any(Ticket.class));
            verify(ticketRepository).save(any(Ticket.class));
        }

        @Test
        @DisplayName("Update ticket successfully by staff")
        void shouldUpdateTicketByStaff() {
            when(authentication.getPrincipal()).thenReturn(testAdmin);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.updateTicket(testTicket.getId(), updateTicketDTO, authentication);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Staff can change reporter")
        void shouldAllowStaffToChangeReporter() {
            UUID newReporterId = UUID.randomUUID();
            User newReporter = User.builder().id(newReporterId).build();
            updateTicketDTO.setReporterId(newReporterId);

            when(authentication.getPrincipal()).thenReturn(testAdmin);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.getReferenceById(newReporterId)).thenReturn(newReporter);
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            ticketService.updateTicket(testTicket.getId(), updateTicketDTO, authentication);

            verify(userRepository).getReferenceById(newReporterId);
        }

        @Test
        @DisplayName("User cannot change reporter")
        void shouldNotAllowRegularUserToChangeReporter() {
            UUID newReporterId = UUID.randomUUID();
            updateTicketDTO.setReporterId(newReporterId);

            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            ticketService.updateTicket(testTicket.getId(), updateTicketDTO, authentication);

            verify(userRepository, never()).getReferenceById(any(UUID.class));
        }

        @Test
        @DisplayName("Throw AccessDenied when user is not authorized")
        void shouldThrowAccessDeniedWhenNotAuthorized() {
            User otherUser = User.builder().id(UUID.randomUUID()).role(UserRole.USER).build();

            when(authentication.getPrincipal()).thenReturn(otherUser);
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));

            assertThatThrownBy(() -> ticketService.updateTicket(testTicket.getId(), updateTicketDTO, authentication))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        @DisplayName("Throw exception when ticket not found")
        void shouldThrowExceptionWhenTicketNotFound() {
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.updateTicket(UUID.randomUUID(), updateTicketDTO, authentication))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Assign ticket Tests")
    class AssignTicketTests {
        @Test
        @DisplayName("Assign ticket to user")
        void shouldAssignTicketToUser() {
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.findById(testAssignee.getId())).thenReturn(Optional.of(testAssignee));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            TicketDTO result = ticketService.assignTicket(testTicket.getId(), testAssignee.getId());

            assertThat(result).isNotNull();
            verify(ticketRepository).save(any(Ticket.class));
        }

        @Test
        @DisplayName("Change status to IN_PROGRESS when assigning OPEN ticket")
        void shouldChangeStatusToInProgressWhenAssigningOpenTicket() {
            testTicket.setStatus(TicketStatus.OPEN);

            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testAssignee));
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
                Ticket saved = invocation.getArgument(0);
                assertThat(saved.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
                assertThat(saved.getAssignee()).isEqualTo(testAssignee);
                return saved;
            });
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            ticketService.assignTicket(testTicket.getId(), testAssignee.getId());

            verify(ticketRepository).save(any(Ticket.class));
        }

        @Test
        @DisplayName("Not change status when ticket is IN_PROGRESS")
        void shouldNotChangeStatusWhenTicketIsInProgress() {
            testTicket.setStatus(TicketStatus.IN_PROGRESS);

            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testAssignee));
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
                Ticket saved = invocation.getArgument(0);
                assertThat(saved.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
                return saved;
            });
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            ticketService.assignTicket(testTicket.getId(), testAssignee.getId());
        }

        @Test
        @DisplayName("Not change status when ticket is CLOSED")
        void shouldNotChangeStatusWhenTicketIsClosed() {
            testTicket.setStatus(TicketStatus.CLOSED);

            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testAssignee));
            when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
                Ticket saved = invocation.getArgument(0);
                assertThat(saved.getStatus()).isEqualTo(TicketStatus.CLOSED);
                return saved;
            });
            when(ticketMapper.toDto(any(Ticket.class))).thenReturn(testTicketDTO);

            ticketService.assignTicket(testTicket.getId(), testAssignee.getId());
        }

        @Test
        @DisplayName("Throw exception when ticket not found")
        void shouldThrowExceptionWhenTicketNotFound() {
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.assignTicket(UUID.randomUUID(), testAssignee.getId()))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Ticket not found");
        }

        @Test
        @DisplayName("Throw exception when assignee not found")
        void shouldThrowExceptionWhenAssigneeNotFound() {
            when(ticketRepository.findById(any(UUID.class))).thenReturn(Optional.of(testTicket));
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.assignTicket(testTicket.getId(), UUID.randomUUID()))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
    }

    @Nested
    @DisplayName("Delete Ticket Tests")
    class DeleteTicketTests {
        @Test
        @DisplayName("Delete ticket successfully")
        void shouldDeleteTicket() {
            when(ticketRepository.existsById(testTicket.getId())).thenReturn(true);
            doNothing().when(ticketRepository).deleteById(testTicket.getId());

            assertThatCode(() -> ticketService.deleteTicket(testTicket.getId()))
                    .doesNotThrowAnyException();

            verify(ticketRepository).deleteById(testTicket.getId());
        }

        @Test
        @DisplayName("Throw exception when ticket not found")
        void shouldThrowExceptionWhenTicketNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(ticketRepository.existsById(nonExistentId)).thenReturn(false);

            assertThatThrownBy(() -> ticketService.deleteTicket(nonExistentId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Ticket not found");

            verify(ticketRepository, never()).deleteById(any(UUID.class));
        }
    }
}
