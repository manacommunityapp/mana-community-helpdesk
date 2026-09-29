package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_subcategories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskSubcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpdeskCategory category;

    @Column(nullable = false)
    private String name;            // e.g. "Plumbing", "Lift", "Electrical"

    private String code;            // e.g. PLUMBING, WATER_LEAKAGE

    @Column(columnDefinition = "TEXT")
    private String keywords;        // comma-separated: "water,leak,pipe,tap,drainage"

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketPriority defaultPriority = TicketPriority.MEDIUM;

    @Builder.Default
    private Integer defaultSlaDurationHours = 24;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer displayOrder = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
