package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private Long communityId;
    private Long reportedByUserId;
    private String reportedByName;
    private String flatNumber;
    private String tower;

    private String rawDescription;
    private String title;

    private HelpdeskCategory category;
    private Long subcategoryId;
    private String subcategoryName;

    private String towerLocation;
    private String floorLocation;
    private String areaLocation;
    private String locationDescription;

    private Long assetId;
    private String assetName;
    private String assetCode;

    private TicketPriority priority;
    private ClassificationSource classificationSource;
    private Integer classificationConfidence;
    private TicketStatus status;
    private EscalationLevel escalationLevel;

    private Long assignedVendorId;
    private String assignedVendorName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private Long assignedStaffUserId;
    private String assignedStaffName;

    private LocalDateTime slaDeadline;
    private Long slaRemainingMinutes;
    private Boolean isSlaBreached;

    private LocalDateTime acknowledgedAt;
    private LocalDateTime workStartedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime cancelledAt;

    private String resolutionNotes;
    private Long resolutionVerifiedByUserId;
    private Integer reopenCount;
    private Boolean emergencyFlag;

    private List<CommentResponse> comments;
    private List<AttachmentResponse> attachments;
    private FeedbackResponse feedback;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
