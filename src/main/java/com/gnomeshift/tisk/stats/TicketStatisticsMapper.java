package com.gnomeshift.tisk.stats;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Map;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface TicketStatisticsMapper {
    @Mapping(target = "totalTickets", source = "totalTickets")
    @Mapping(target = "unassignedTickets", source = "unassignedTickets")
    @Mapping(target = "ticketsByStatus", source = "ticketsByStatus")
    @Mapping(target = "ticketsByPriority", source = "ticketsByPriority")
    @Mapping(target = "createdToday", source = "createdToday")
    @Mapping(target = "createdThisWeek", source = "createdThisWeek")
    @Mapping(target = "createdThisMonth", source = "createdThisMonth")
    @Mapping(target = "closedToday", source = "closedToday")
    @Mapping(target = "closedThisWeek", source = "closedThisWeek")
    @Mapping(target = "closedThisMonth", source = "closedThisMonth")
    @Mapping(target = "averageResolutionTimeHours", expression = "java(convertSecondsToHours(data.getAverageResolutionTimeSeconds()))")
    @Mapping(target = "openPercentage", expression = "java(calculatePercentage(data.getTicketsByStatus(), \"OPEN\", data.getTotalTickets()))")
    @Mapping(target = "inProgressPercentage", expression = "java(calculatePercentage(data.getTicketsByStatus(), \"IN_PROGRESS\", data.getTotalTickets()))")
    @Mapping(target = "closedPercentage", expression = "java(calculatePercentage(data.getTicketsByStatus(), \"CLOSED\", data.getTotalTickets()))")
    TicketStatisticsDTO toDto(TicketStatistics data);

    default Double convertSecondsToHours(Double seconds) {
        if (seconds == null) {
            return null;
        }
        return Math.round(seconds / 3600.0 * 100.0) / 100.0;
    }

    default double calculatePercentage(Map<String, Long> tickets, String status, long total) {
        if (total == 0 || tickets == null) {
            return 0.0;
        }
        double percentage = tickets.getOrDefault(status, 0L) * 100.0 / total;
        return Math.round(percentage * 100.0) / 100.0;
    }
}
