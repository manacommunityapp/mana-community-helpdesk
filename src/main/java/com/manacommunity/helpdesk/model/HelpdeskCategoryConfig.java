package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskCategoryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpdeskCategory category;

    @Column(nullable = false)
    private String name;           // Display name e.g. "Maintenance"

    private String icon;           // emoji or icon code

    @Builder.Default
    private Integer displayOrder = 0;

    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
