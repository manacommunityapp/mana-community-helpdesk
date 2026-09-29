package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {
    private Long id;
    private Long communityId;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String specializations;
    private String serviceAreas;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private Boolean active;
    private Integer totalTicketsCompleted;
    private Double averageResponseMinutes;
    private Double averageResolutionMinutes;
    private Double slaCompliancePercent;
    private Double averageRating;
    private Integer reopenedTickets;
}
