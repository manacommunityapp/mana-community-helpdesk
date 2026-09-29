package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.AssetStatus;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
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
public class AssetRequest {
    @NotBlank(message = "Asset name is required")
    private String name;

    @NotBlank(message = "Asset code is required")
    private String assetCode;

    @NotNull(message = "Category is required")
    private HelpdeskCategory category;

    private String towerLocation;
    private String floorLocation;
    private String areaLocation;
    private String description;

    private LocalDate installationDate;
    private LocalDate warrantyExpiry;
    private AssetStatus status;

    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDue;
    private Long vendorId;
}
