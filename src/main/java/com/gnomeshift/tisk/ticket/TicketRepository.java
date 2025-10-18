package com.gnomeshift.tisk.ticket;


import com.gnomeshift.tisk.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findByReporter(User reporter, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:reporterId IS NULL OR t.reporter.id = :reporterId) AND " +
            "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
            "(:createdAt IS NULL OR t.createdAt >= :createdAt)")
    Page<Ticket> findWithFilters(@Param("search") String search,
                                 @Param("status") TicketStatus status,
                                 @Param("priority") TicketPriority priority,
                                 @Param("reporterId") UUID reporterId,
                                 @Param("assigneeId") UUID assigneeId,
                                 @Param("createdFrom") LocalDateTime createdAt,
                                 Pageable pageable);
    LocalDateTime createdAt(LocalDateTime createdAt);
}
