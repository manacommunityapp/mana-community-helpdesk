package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryResponse {
    private Long id;
    private Long communityId;
    private HelpdeskCategory category;
    private String name;
    private String code;
    private String keywords;
    private TicketPriority defaultPriority;
    private Integer defaultSlaDurationHours;
    private Boolean active;
    private Integer displayOrder;
}
