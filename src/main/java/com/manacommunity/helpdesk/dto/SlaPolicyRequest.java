package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.TicketPriority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlaPolicyRequest {
    @NotNull(message = "Priority is required")
    private TicketPriority priority;

    @NotNull(message = "Resolution hours is required")
    private Integer resolutionHours;

    private Integer escalation75PercentMinutes;
    private Integer escalation90PercentMinutes;
    private Boolean active;
}
