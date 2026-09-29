package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignTicketRequest {
    private Long vendorId;
    private Long technicianId;
    private Long staffUserId;
    private String notes;
}
