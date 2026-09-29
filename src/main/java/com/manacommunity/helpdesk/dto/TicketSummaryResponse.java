package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import com.manacommunity.helpdesk.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSummaryResponse {
    private Long id;
    private String ticketNumber;
    private String title;
    private String rawDescription;
    private HelpdeskCategory category;
    private String subcategoryName;
    private TicketPriority priority;
    private TicketStatus status;
    private String tower;
    private String flatNumber;
    private String assignedTechnicianName;
    private LocalDateTime slaDeadline;
    private Long slaRemainingMinutes;
    private Boolean isSlaBreached;
    private Boolean emergencyFlag;
    private LocalDateTime createdAt;
}
