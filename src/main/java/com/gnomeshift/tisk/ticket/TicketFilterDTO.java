package com.gnomeshift.tisk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketFilterDTO {
    private String search;
    private TicketStatus status;
    private TicketPriority priority;
    private UUID reporterId;
    private UUID assigneeId;
    private LocalDate createdAt;
}
