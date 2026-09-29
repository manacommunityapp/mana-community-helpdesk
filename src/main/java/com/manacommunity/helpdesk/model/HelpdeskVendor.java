package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long communityId;

    @Column(nullable = false)
    private String name;

    private String contactPerson;
    private String phone;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String specializations; // Comma separated: PLUMBING, ELECTRICAL, LIFT, etc.

    @Column(columnDefinition = "TEXT")
    private String serviceAreas;    // Comma separated: Tower A, Tower B, etc.

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;

    @Builder.Default
    private Boolean active = true;

    // Metrics for scorecards
    @Builder.Default
    private Integer totalTicketsCompleted = 0;

    @Builder.Default
    private Double averageResponseMinutes = 0.0;

    @Builder.Default
    private Double averageResolutionMinutes = 0.0;

    @Builder.Default
    private Double slaCompliancePercent = 100.0;

    @Builder.Default
    private Double averageRating = 5.0;

    @Builder.Default
    private Integer reopenedTickets = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
