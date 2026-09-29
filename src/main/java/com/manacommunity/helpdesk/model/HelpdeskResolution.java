package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_resolutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long ticketId;

    private Long resolvedByUserId;
    private String resolvedByName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resolutionNotes;

    private String beforePhotoUrl;
    private String afterPhotoUrl;

    private LocalDateTime resolutionVerifiedAt;
    private Long verifiedByUserId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
