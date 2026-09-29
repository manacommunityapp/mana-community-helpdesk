package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.WorkOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderResponse {
    private Long id;
    private Long communityId;
    private Long assetId;
    private String assetName;
    private String assetCode;
    private String title;
    private String description;
    private WorkOrderType workOrderType;
    private LocalDate scheduledDate;
    private String status;
    private Long assignedVendorId;
    private String assignedVendorName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
