package com.gnomeshift.tisk.stats;

import java.time.LocalDate;

public interface DailyCount {
    LocalDate getDate();
    Long getCreated();
    Long getClosed();
}
