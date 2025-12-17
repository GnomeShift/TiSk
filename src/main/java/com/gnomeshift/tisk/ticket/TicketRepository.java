package com.gnomeshift.tisk.ticket;


import com.gnomeshift.tisk.stats.AssigneeCount;
import com.gnomeshift.tisk.stats.DailyCount;
import com.gnomeshift.tisk.stats.PriorityCount;
import com.gnomeshift.tisk.stats.StatusCount;
import com.gnomeshift.tisk.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByReporter(User reporter);

    @Query("SELECT t.status as status, COUNT(t) as count FROM Ticket t GROUP BY t.status")
    List<StatusCount> countByStatus();

    @Query("SELECT t.priority as priority, COUNT(t) as count FROM Ticket t GROUP BY t.priority")
    List<PriorityCount> countByPriority();

    long countByAssigneeIsNull();
    long countByCreatedAtAfter(LocalDateTime dateTime);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'CLOSED' AND t.updatedAt >= :dateTime")
    long countByStatusClosedAfter(LocalDateTime dateTime);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'CLOSED' AND t.updatedAt BETWEEN :startDate AND :endDate")
    long countByStatusClosedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (updated_at - created_at))) FROM tickets WHERE status = 'CLOSED'", nativeQuery = true)
    Double countAverageResolutionTime();

    @Query(value = """
        SELECT
            t.assignee_id as assigneeId,
            u.first_name as firstName,
            u.last_name as lastName,
            u.email as email,
            COUNT(t.id) as totalCount,
            SUM(CASE WHEN t.status = 'OPEN' THEN 1 ELSE 0 END) as openCount,
            SUM(CASE WHEN t.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressCount,
            SUM(CASE WHEN t.status = 'CLOSED' THEN 1 ELSE 0 END) as closedCount,
            AVG(CASE WHEN t.status = 'CLOSED' THEN EXTRACT(EPOCH FROM (t.updated_at - t.created_at)) END) as averageResolutionTime
        FROM tickets t
        JOIN users u ON t.assignee_id = u.id
        WHERE t.assignee_id IS NOT NULL
        GROUP BY t.assignee_id, u.first_name, u.last_name, u.email
        ORDER BY totalCount DESC
    """, nativeQuery = true)
    List<AssigneeCount> countAllAssigneesStatistics();

    @Query(value = """
        SELECT
            t.assignee_id as assigneeId,
            u.first_name as firstName,
            u.last_name as lastName,
            u.email as email,
            COUNT(t.id) as totalCount,
            SUM(CASE WHEN t.status = 'OPEN' THEN 1 ELSE 0 END) as openCount,
            SUM(CASE WHEN t.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressCount,
            SUM(CASE WHEN t.status = 'CLOSED' THEN 1 ELSE 0 END) as closedCount,
            AVG(CASE WHEN t.status = 'CLOSED' THEN EXTRACT(EPOCH FROM (t.updated_at - t.created_at)) END) as averageResolutionTime
        FROM tickets t
        JOIN users u ON t.assignee_id = u.id
        WHERE t.assignee_id = :assigneeId
        GROUP BY t.assignee_id, u.first_name, u.last_name, u.email
    """, nativeQuery = true)
    AssigneeCount countStatisticsByAssigneeId(UUID assigneeId);

    @Query("""
        SELECT
            CAST(t.createdAt AS LocalDate) AS date,
            COUNT(t) AS created,
            SUM(CASE WHEN t.status = 'CLOSED' THEN 1L ELSE 0L END) AS closed
        FROM Ticket t
        WHERE CAST(t.createdAt AS LocalDate) BETWEEN :startDate AND :endDate
        GROUP BY CAST(t.createdAt AS LocalDate)
        ORDER BY CAST(t.createdAt AS LocalDate)
        """)
    List<DailyCount> getDailyStatistics(LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT t.reporter.department as department, COUNT(t) as count
        FROM Ticket t
        WHERE t.reporter.department IS NOT NULL
        GROUP BY t.reporter.department
        ORDER BY count DESC
    """)
    List<Object[]> countTicketsByDepartment();
}
