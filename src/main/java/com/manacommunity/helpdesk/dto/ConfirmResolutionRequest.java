package com.manacommunity.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmResolutionRequest {
    @NotNull(message = "Resolved status is required")
    private Boolean isResolved; // true = close request, false = reopen
    private String reopenReason;
}
