package com.manacommunity.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "manacommunity", name = "helpdesk_technicians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpdeskTechnician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vendorId;

    @Column(nullable = false)
    private Long communityId;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String specialization;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer currentWorkload = 0; // Count of open assigned tickets

    @CreationTimestamp
    private LocalDateTime createdAt;
}
