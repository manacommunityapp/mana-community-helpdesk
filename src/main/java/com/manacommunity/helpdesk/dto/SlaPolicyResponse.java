package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlaPolicyResponse {
    private Long id;
    private Long communityId;
    private TicketPriority priority;
    private Integer resolutionHours;
    private Integer escalation75PercentMinutes;
    private Integer escalation90PercentMinutes;
    private Boolean active;
}
