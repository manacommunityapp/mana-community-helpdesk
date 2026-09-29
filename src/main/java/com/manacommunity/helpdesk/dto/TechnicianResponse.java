package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianResponse {
    private Long id;
    private Long vendorId;
    private String vendorName;
    private Long communityId;
    private String name;
    private String phone;
    private String specialization;
    private Boolean active;
    private Integer currentWorkload;
}
