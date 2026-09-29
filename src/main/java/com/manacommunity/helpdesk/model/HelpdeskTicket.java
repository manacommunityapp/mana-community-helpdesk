package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String ticketNumber;                   // HD-20260925-0042

    @Column(nullable = false)
    private Long communityId;

    @Column(nullable = false)
    private Long reportedByUserId;

    private String reportedByName;
    private String flatNumber;
    private String tower;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rawDescription;                 // what resident typed

    private String title;

    @Enumerated(EnumType.STRING)
    private HelpdeskCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private HelpdeskSubcategory subcategory;

    // Structured location
    private String towerLocation;
    private String floorLocation;
    private String areaLocation;
    private String locationDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private HelpdeskAsset asset;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ClassificationSource classificationSource = ClassificationSource.RULE_DETERMINED;

    @Builder.Default
    private Integer classificationConfidence = 0;   // 0–100

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EscalationLevel escalationLevel = EscalationLevel.NONE;

    // Assignment
    private Long assignedVendorId;
    private String assignedVendorName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private Long assignedStaffUserId;
    private String assignedStaffName;

    // SLA
    private LocalDateTime slaDeadline;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime workStartedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime cancelledAt;

    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;

    private Long resolutionVerifiedByUserId;

    @Builder.Default
    private Integer reopenCount = 0;

    @Builder.Default
    private Boolean emergencyFlag = false;         // set when keywords match fire/gas/trapped

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
