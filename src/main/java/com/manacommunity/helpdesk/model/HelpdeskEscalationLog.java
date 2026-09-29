package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.EscalationLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_escalation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskEscalationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EscalationLevel escalationLevel;

    private Long notifiedUserId;
    private String notifiedRole; // STAFF, SUPERVISOR, ADMIN

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private LocalDateTime triggeredAt;
}
