package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketStatusRequest {
    @NotNull(message = "New status is required")
    private TicketStatus newStatus;
    private String reason;
}
