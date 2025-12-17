package com.gnomeshift.tisk.stats;

import java.util.UUID;

public interface AssigneeCount {
    UUID getAssigneeId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Long getTotalCount();
    Long getOpenCount();
    Long getInProgressCount();
    Long getClosedCount();
    Double getAverageResolutionTime();
}
