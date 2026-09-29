package com.manacommunity.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorRequest {
    @NotBlank(message = "Vendor name is required")
    private String name;

    private String contactPerson;
    private String phone;
    private String email;
    private String specializations;
    private String serviceAreas;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private Boolean active;
}
