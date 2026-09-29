package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalOpen;
    private long totalInProgress;
    private long totalDueSoon;
    private long totalSlaBreached;
    private long totalResolvedToday;
    private Map<String, Long> countByCategory;
    private Map<String, Long> countByPriority;
}
