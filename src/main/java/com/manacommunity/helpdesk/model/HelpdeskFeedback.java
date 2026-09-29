package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long ticketId;

    @Column(nullable = false)
    private Long communityId;

    @Column(nullable = false)
    private Long userId;

    private Integer overallRating;          // 1-5
    private Integer resolutionQualityRating; // 1-5
    private Integer responseTimeRating;      // 1-5

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
