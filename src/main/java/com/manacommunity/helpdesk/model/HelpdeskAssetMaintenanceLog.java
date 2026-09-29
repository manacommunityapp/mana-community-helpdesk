package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_asset_maintenance_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskAssetMaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private Long communityId;

    private Long ticketId;
    private Long workOrderId;

    private String maintenanceType; // PREVENTIVE, BREAKDOWN, ROUTINE_CHECK
    private LocalDateTime performedAt;

    private Long performedByVendorId;
    private Long technicianId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private BigDecimal cost;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
