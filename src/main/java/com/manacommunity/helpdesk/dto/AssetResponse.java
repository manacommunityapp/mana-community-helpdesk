package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.AssetStatus;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
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
public class AssetResponse {
    private Long id;
    private Long communityId;
    private String name;
    private String assetCode;
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
    private String vendorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
