package com.gnomeshift.tisk.stats;

import com.gnomeshift.tisk.ticket.TicketPriority;
import com.gnomeshift.tisk.ticket.TicketRepository;
import com.gnomeshift.tisk.ticket.TicketStatus;
import com.gnomeshift.tisk.user.User;
import com.gnomeshift.tisk.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatisticsService {
    private final TicketRepository ticketRepository;
    private final TicketStatisticsMapper ticketStatisticsMapper;
    private final AssigneeStatisticsMapper assigneeStatisticsMapper;
    private final PeriodStatisticsMapper periodStatisticsMapper;
    private final UserRepository userRepository;

    public TicketStatisticsDTO getAllStatistics() {
        log.info("Fetching all statistics");

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfWeek = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).atStartOfDay();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        return ticketStatisticsMapper.toDto(
                TicketStatistics.builder()
                .totalTickets(ticketRepository.count())
                .unassignedTickets(ticketRepository.countByAssigneeIsNull())
                .ticketsByStatus(getTicketsByStatus())
                .ticketsByPriority(getTicketsByPriority())
                .createdToday(ticketRepository.countByCreatedAtAfter(startOfToday))
                .createdThisWeek(ticketRepository.countByCreatedAtAfter(startOfWeek))
                .createdThisMonth(ticketRepository.countByCreatedAtAfter(startOfMonth))
                .closedToday(ticketRepository.countByStatusClosedAfter(startOfToday))
                .closedThisWeek(ticketRepository.countByStatusClosedAfter(startOfWeek))
                .closedThisMonth(ticketRepository.countByStatusClosedAfter(startOfMonth))
                .averageResolutionTimeSeconds(ticketRepository.countAverageResolutionTime())
                .build()
        );
    }

    public Map<String, Long> getTicketsByStatus() {
        return mapCountsToEnum(ticketRepository.countByStatus(), TicketStatus.values(), StatusCount::getStatus, StatusCount::getCount);
    }

    public Map<String, Long> getTicketsByPriority() {
        return mapCountsToEnum(ticketRepository.countByPriority(), TicketPriority.values(), PriorityCount::getPriority, PriorityCount::getCount);
    }

    public Map<String, Long> getTicketsByDepartment() {
        log.info("Fetching statistics by department");

        return ticketRepository.countTicketsByDepartment().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public List<AssigneeStatisticsDTO> getAllAssigneesStatistics() {
        log.info("Fetching assignee statistics");
        return assigneeStatisticsMapper.toDtoList(ticketRepository.countAllAssigneesStatistics());
    }

    public AssigneeStatisticsDTO getAssigneeStatisticsById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        AssigneeCount count = ticketRepository.countStatisticsByAssigneeId(id);

        if (count == null) {
            throw new EntityNotFoundException("No tickets found for assignee: " + id);
        }
        return assigneeStatisticsMapper.toDto(count);
    }

    public PeriodStatisticsDTO getPeriodStatistics(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching period statistics from {} to {}", startDate, endDate);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return periodStatisticsMapper.toDto(
                PeriodStatistics.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalCreated(ticketRepository.countByCreatedAtBetween(start, end))
                .totalClosed(ticketRepository.countByStatusClosedBetween(start, end))
                .dailyCounts(ticketRepository.getDailyStatistics(startDate, endDate))
                .build()
        );
    }

    public AssigneeStatisticsDTO getMyStatistics(String email) {
        User assignee = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        AssigneeCount count = ticketRepository.countStatisticsByAssigneeId(assignee.getId());

        if (count == null) {
            throw new EntityNotFoundException("No tickets found for assignee: " + assignee.getId());
        }
        return assigneeStatisticsMapper.toDto(count);
    }

    private <T, E extends Enum<E>> Map<String, Long> mapCountsToEnum(List<T> counts, E[] enumValues,
                                                                     Function<T, String> keyExtractor,
                                                                     Function<T, Long> valueExtractor) {
        Map<String, Long> result = new LinkedHashMap<>();

        for (E enumValue : enumValues) {
            result.put(enumValue.name(), 0L);
        }

        for (T count : counts) {
            result.put(keyExtractor.apply(count), valueExtractor.apply(count));
        }
        return result;
    }
}
