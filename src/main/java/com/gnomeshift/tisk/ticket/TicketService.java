package com.gnomeshift.tisk.ticket;

import com.gnomeshift.tisk.user.User;
import com.gnomeshift.tisk.user.UserRepository;
import com.gnomeshift.tisk.user.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    // Get current user from session
    private User getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }

        log.warn("Current user not found");
        throw new EntityNotFoundException("User not found");
    }

    private boolean isStaff(User user) {
        return user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SUPPORT;
    }

    // Centralized access check
    private void checkAccess(Ticket ticket, User user) {
        if (isStaff(user)) {
            return;
        }

        // Compare user UUID
        boolean isReporter = ticket.getReporter().getId().equals(user.getId());
        boolean isAssignee = ticket.getAssignee() != null && ticket.getAssignee().getId().equals(user.getId());

        if (!isReporter && !isAssignee) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TicketDTO getTicketById(UUID id, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        User user = getCurrentUser(authentication);
        checkAccess(ticket, user);
        return ticketMapper.toDto(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getMyTickets(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return ticketMapper.toDtoList(ticketRepository.findAllByReporter(user));
    }

    @Transactional
    public TicketDTO createTicket(CreateTicketDTO createTicketDTO, Authentication authentication) {
        log.info("Creating new ticket with title: {}", createTicketDTO.getTitle());

        User user = getCurrentUser(authentication);
        Ticket ticket = ticketMapper.toEntity(createTicketDTO);

        // Optimization by proxy-object
        User reporterRef;

        if (isStaff(user) && createTicketDTO.getReporterId() != null) {
            // Staff can create a ticket for someone
            reporterRef = userRepository.getReferenceById(createTicketDTO.getReporterId());
        }
        else {
            // Use current user ID to get a reference
            reporterRef = userRepository.getReferenceById(user.getId());
        }

        ticket.setReporter(reporterRef);
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket created successfully with id: {}", savedTicket.getId());
        return ticketMapper.toDto(savedTicket);
    }

    @Transactional
    public TicketDTO updateTicket(UUID id, UpdateTicketDTO updateTicketDTO, Authentication authentication) {
        log.info("Updating ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        User currentUser = getCurrentUser(authentication);
        checkAccess(ticket, currentUser);
        ticketMapper.updateTicketFromDto(updateTicketDTO, ticket);

        // Staff can change reporter
        if (isStaff(currentUser) && updateTicketDTO.getReporterId() != null) {
            // Use current user ID to get a reference
            ticket.setReporter(userRepository.getReferenceById(updateTicketDTO.getReporterId()));
        }

        log.info("Ticket updated successfully: {}", id);
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO assignTicket(UUID id, UUID assigneeId) {
        log.info("Assigning ticket {} to user {}", id, assigneeId);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));

        ticket.setAssignee(assignee);

        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        log.info("Ticket assigned successfully: {}", id);
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public void deleteTicket(UUID id) {
        log.info("Deleting ticket with id: {}", id);

        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }

        ticketRepository.deleteById(id);
        log.info("Ticket deleted successfully: {}", id);
    }
}
