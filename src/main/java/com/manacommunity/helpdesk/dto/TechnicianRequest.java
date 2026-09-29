package com.manacommunity.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianRequest {
    private Long vendorId;

    @NotBlank(message = "Technician name is required")
    private String name;

    private String phone;
    private String specialization;
    private Boolean active;
}
