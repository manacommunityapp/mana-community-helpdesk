package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorScorecardResponse {
    private Long vendorId;
    private String vendorName;
    private Integer ticketsCompleted;
    private Double avgResponseMinutes;
    private Double avgResolutionMinutes;
    private Double slaCompliancePercent;
    private Double residentRating;
    private Integer reopenedTickets;
}
