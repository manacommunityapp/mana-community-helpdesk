package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDashboardResponse {
    private long activeCount;
    private long criticalCount;
    private long resolvedCount;
    private List<TicketSummaryResponse> activeTickets;
}
