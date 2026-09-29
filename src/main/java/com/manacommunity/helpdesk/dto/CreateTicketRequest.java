package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    @NotBlank(message = "Problem description is required")
    private String rawDescription;

    private String title;
    private HelpdeskCategory category;
    private Long subcategoryId;
    private TicketPriority priority;

    // Location
    private String tower;
    private String flatNumber;
    private String towerLocation;
    private String floorLocation;
    private String areaLocation;
    private String locationDescription;

    private Long assetId;

    // Optional initial photos/files
    private List<String> attachmentUrls;
}
