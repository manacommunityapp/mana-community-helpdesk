package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private Map<String, Long> requestsByCategory;
    private Map<String, Long> requestsByTower;
    private Map<String, Long> requestsByPriority;
    private List<String> recurringIssueAlerts; // e.g. "Tower B basement has received 7 water-leakage complaints in 30 days"
    private Double avgResolutionHours;
    private Double overallSlaCompliancePercent;
}
