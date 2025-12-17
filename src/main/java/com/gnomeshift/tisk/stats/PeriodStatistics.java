package com.gnomeshift.tisk.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodStatistics {
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalCreated;
    private long totalClosed;
    private List<DailyCount> dailyCounts;
}
