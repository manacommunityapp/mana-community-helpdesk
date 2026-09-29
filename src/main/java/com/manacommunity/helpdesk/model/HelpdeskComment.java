package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Long communityId;

    private Long authorUserId;
    private String authorName;
    private String authorRole;    // RESIDENT, STAFF, VENDOR, TECHNICIAN, ADMIN

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Builder.Default
    private Boolean internalOnly = false;  // hidden from residents

    @CreationTimestamp
    private LocalDateTime createdAt;
}
