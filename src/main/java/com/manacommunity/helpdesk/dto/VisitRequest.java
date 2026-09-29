package com.manacommunity.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitRequest {
    @NotNull(message = "Technician ID is required")
    private Long technicianId;

    private LocalDateTime scheduledAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;
    private String notes;
}
