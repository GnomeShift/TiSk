package com.gnomeshift.tisk.ticket;

import com.gnomeshift.tisk.user.User;
import com.gnomeshift.tisk.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    @Transactional(readOnly = true)
    public Page<TicketDTO> getAllTickets(Pageable pageable, TicketFilterDTO filter) {
        LocalDateTime createdAt = filter.getCreatedAt() != null ? filter.getCreatedAt().atStartOfDay() : null;
        Page<Ticket> tickets = ticketRepository.findWithFilters(
                filter.getSearch(),
                filter.getStatus(),
                filter.getPriority(),
                filter.getReporterId(),
                filter.getAssigneeId(),
                createdAt,
                pageable
        );
        return tickets.map(ticketMapper::toDto);
    }

    @Transactional(readOnly = true)
    public TicketDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id '" + id + "' not found"));
        return ticketMapper.toDto(ticket);
    }

    @Transactional(readOnly = true)
    public Page<TicketDTO> getMyTickets(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email '" + email + "' not found"));

        Page<Ticket> tickets = ticketRepository.findByReporter(user, pageable);
        return tickets.map(ticketMapper::toDto);
    }

    @Transactional
    public TicketDTO createTicket(CreateTicketDTO createTicketDTO) {
        log.info("Creating new ticket with title: {}", createTicketDTO.getTitle());

        User reporter = userRepository.findById(createTicketDTO.getReporterId())
                .orElseThrow(() -> new EntityNotFoundException("User with id '" + createTicketDTO.getReporterId() + "' not found"));

        Ticket ticket = ticketMapper.toEntity(createTicketDTO);
        ticket.setReporter(reporter);
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO updateTicket(UUID id, UpdateTicketDTO updateTicketDTO) {
        log.info("Updating ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id '" + id + "' not found"));

        ticketMapper.updateTicketFromDto(updateTicketDTO, ticket);

        if (updateTicketDTO.getReporterId() != null) {
            User reporter = userRepository.findById(updateTicketDTO.getReporterId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' not found"));
            ticket.setAssignee(reporter);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket updated successfully: {}", id);
        return ticketMapper.toDto(savedTicket);
    }

    @Transactional
    public TicketDTO assignTicket(UUID id, UUID assigneeId) {
        log.info("Assigning ticket {} to user {}", id, assigneeId);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id '" + id + "' not found"));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' not found"));

        ticket.setAssignee(assignee);

        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket assigned successfully");
        return ticketMapper.toDto(savedTicket);
    }

    @Transactional
    public void deleteTicket(UUID id) {
        log.info("Deleting ticket with id: {}", id);

        ticketRepository.deleteById(id);
        log.info("Ticket deleted successfully: {}", id);
    }
}
