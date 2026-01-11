package com.gnomeshift.tisk.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatistics {
    private long totalTickets;
    private long unassignedTickets;
    private Map<String, Long> ticketsByStatus;
    private Map<String, Long> ticketsByPriority;
    private long createdToday;
    private long createdThisWeek;
    private long createdThisMonth;
    private long closedToday;
    private long closedThisWeek;
    private long closedThisMonth;
    private Double averageResolutionTimeSeconds;
}
