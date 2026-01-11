package com.gnomeshift.tisk.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStatisticsDTO {
    private LocalDate date;
    private long created;
    private long closed;
}
