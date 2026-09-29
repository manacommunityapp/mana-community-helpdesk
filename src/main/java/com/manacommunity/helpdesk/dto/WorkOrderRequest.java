package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.WorkOrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {
    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private WorkOrderType workOrderType;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    private Long assignedVendorId;
    private Long assignedTechnicianId;
    private String notes;
}
