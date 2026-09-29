package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
    private Long id;
    private Long ticketId;
    private Long technicianId;
    private String technicianName;
    private LocalDateTime scheduledAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
}
