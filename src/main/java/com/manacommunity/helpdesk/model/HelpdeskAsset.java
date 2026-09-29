package com.manacommunity.helpdesk.model;

import com.manacommunity.helpdesk.enums.AssetStatus;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long communityId;

    @Column(nullable = false)
    private String name; // e.g. "Tower A Lift 1", "DG Set 500kVA"

    @Column(nullable = false, length = 50)
    private String assetCode; // e.g. "LIFT-A-01", "DG-MAIN"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpdeskCategory category;

    private String towerLocation;
    private String floorLocation;
    private String areaLocation;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate installationDate;
    private LocalDate warrantyExpiry;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AssetStatus status = AssetStatus.ACTIVE;

    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDue;

    private Long vendorId; // Assigned AMC / Maintenance vendor

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
