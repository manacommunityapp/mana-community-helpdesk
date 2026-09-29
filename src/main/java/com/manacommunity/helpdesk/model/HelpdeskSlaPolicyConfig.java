package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.TicketPriority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_sla_policies",
       uniqueConstraints = @UniqueConstraint(columnNames = {"communityId", "priority"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskSlaPolicyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    // Resolution SLA in hours (0 = immediate)
    @Builder.Default
    private Integer resolutionHours = 24;

    // When to escalate (minutes from creation)
    @Builder.Default
    private Integer escalation75PercentMinutes = 1080;   // 75% of 24h = 18h

    @Builder.Default
    private Integer escalation90PercentMinutes = 1296;   // 90% of 24h = 21.6h

    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
