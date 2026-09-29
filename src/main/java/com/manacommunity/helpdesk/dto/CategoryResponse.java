package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private Long communityId;
    private HelpdeskCategory category;
    private String name;
    private String icon;
    private Integer displayOrder;
    private Boolean active;
}
