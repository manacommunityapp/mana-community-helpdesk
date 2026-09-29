package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Long communityId;

    @Enumerated(EnumType.STRING)
    private TicketStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus toStatus;

    private Long changedByUserId;
    private String changedByName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
