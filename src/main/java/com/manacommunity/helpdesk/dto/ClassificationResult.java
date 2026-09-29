package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.ClassificationSource;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResult {
    private HelpdeskCategory category;
    private Long subcategoryId;
    private String subcategoryName;
    private TicketPriority priority;
    private Integer suggestedSlaHours;
    private Integer confidence; // 0-100
    private Boolean emergencyFlag;
    private ClassificationSource source;
    private String suggestedTeam;
}
