package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Category enum is required")
    private HelpdeskCategory category;

    @NotBlank(message = "Category display name is required")
    private String name;

    private String icon;
    private Integer displayOrder;
    private Boolean active;
}
