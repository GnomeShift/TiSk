package com.gnomeshift.tisk.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<TicketStatisticsDTO> getOverallStatistics() {
        return ResponseEntity.ok(statisticsService.getAllStatistics());
    }
    @GetMapping("/by-status")
    public ResponseEntity<Map<String, Long>> getStatisticsByStatus() {
        return ResponseEntity.ok(statisticsService.getTicketsByStatus());
    }

    @GetMapping("/by-priority")
    public ResponseEntity<Map<String, Long>> getStatisticsByPriority() {
        return ResponseEntity.ok(statisticsService.getTicketsByPriority());
    }

    @GetMapping("/assignees")
    public ResponseEntity<List<AssigneeStatisticsDTO>> getAllAssigneesStatistics() {
        return ResponseEntity.ok(statisticsService.getAllAssigneesStatistics());
    }

    @GetMapping("/assignees/{id}")
    public ResponseEntity<AssigneeStatisticsDTO> getAssigneeStatisticsById(@PathVariable UUID id) {
        return ResponseEntity.ok(statisticsService.getAssigneeStatisticsById(id));
    }

    @GetMapping("/period")
    public ResponseEntity<PeriodStatisticsDTO> getPeriodStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statisticsService.getPeriodStatistics(startDate, endDate));
    }

    @GetMapping("/last-days/{days}")
    public ResponseEntity<PeriodStatisticsDTO> getLastDaysStatistics(@PathVariable int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        return ResponseEntity.ok(statisticsService.getPeriodStatistics(startDate, endDate));
    }

    @GetMapping("/by-department")
    public ResponseEntity<Map<String, Long>> getStatisticsByDepartment() {
        return ResponseEntity.ok(statisticsService.getTicketsByDepartment());
    }

    @GetMapping("/my")
    public ResponseEntity<AssigneeStatisticsDTO> getMyStatistics(Authentication authentication) {
        return ResponseEntity.ok(statisticsService.getMyStatistics(authentication.getName()));
    }
}
