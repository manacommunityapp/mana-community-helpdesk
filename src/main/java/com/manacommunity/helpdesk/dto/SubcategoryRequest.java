package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
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
public class SubcategoryRequest {
    @NotNull(message = "Parent category is required")
    private HelpdeskCategory category;

    @NotBlank(message = "Subcategory name is required")
    private String name;

    private String code;
    private String keywords;
    private TicketPriority defaultPriority;
    private Integer defaultSlaDurationHours;
    private Boolean active;
    private Integer displayOrder;
}
