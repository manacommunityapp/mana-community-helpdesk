package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.CategoryRequest;
import com.manacommunity.helpdesk.dto.CategoryResponse;
import com.manacommunity.helpdesk.dto.SubcategoryRequest;
import com.manacommunity.helpdesk.dto.SubcategoryResponse;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.model.HelpdeskCategoryConfig;
import com.manacommunity.helpdesk.model.HelpdeskSubcategory;
import com.manacommunity.helpdesk.repository.HelpdeskCategoryConfigRepository;
import com.manacommunity.helpdesk.repository.HelpdeskSubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final HelpdeskCategoryConfigRepository categoryConfigRepository;
    private final HelpdeskSubcategoryRepository subcategoryRepository;

    public List<CategoryResponse> getCategories(Long communityId) {
        return categoryConfigRepository.findByCommunityIdAndActiveTrueOrderByDisplayOrderAsc(communityId)
                .stream().map(this::mapCategory).collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest req, Long communityId) {
        HelpdeskCategoryConfig config = HelpdeskCategoryConfig.builder()
                .communityId(communityId)
                .category(req.getCategory())
                .name(req.getName())
                .icon(req.getIcon())
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .active(req.getActive() != null ? req.getActive() : true)
                .build();
        return mapCategory(categoryConfigRepository.save(config));
    }

    public List<SubcategoryResponse> getSubcategories(Long communityId, HelpdeskCategory category) {
        List<HelpdeskSubcategory> list = (category != null)
                ? subcategoryRepository.findByCommunityIdAndCategoryAndActiveTrueOrderByDisplayOrderAsc(communityId, category)
                : subcategoryRepository.findByCommunityIdAndActiveTrue(communityId);
        return list.stream().map(this::mapSubcategory).collect(Collectors.toList());
    }

    @Transactional
    public SubcategoryResponse createSubcategory(SubcategoryRequest req, Long communityId) {
        HelpdeskSubcategory sub = HelpdeskSubcategory.builder()
                .communityId(communityId)
                .category(req.getCategory())
                .name(req.getName())
                .code(req.getCode() != null ? req.getCode() : req.getName().toUpperCase().replace(" ", "_"))
                .keywords(req.getKeywords())
                .defaultPriority(req.getDefaultPriority())
                .defaultSlaDurationHours(req.getDefaultSlaDurationHours() != null ? req.getDefaultSlaDurationHours() : 24)
                .active(req.getActive() != null ? req.getActive() : true)
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();
        return mapSubcategory(subcategoryRepository.save(sub));
    }

    private CategoryResponse mapCategory(HelpdeskCategoryConfig c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .communityId(c.getCommunityId())
                .category(c.getCategory())
                .name(c.getName())
                .icon(c.getIcon())
                .displayOrder(c.getDisplayOrder())
                .active(c.getActive())
                .build();
    }

    private SubcategoryResponse mapSubcategory(HelpdeskSubcategory s) {
        return SubcategoryResponse.builder()
                .id(s.getId())
                .communityId(s.getCommunityId())
                .category(s.getCategory())
                .name(s.getName())
                .code(s.getCode())
                .keywords(s.getKeywords())
                .defaultPriority(s.getDefaultPriority())
                .defaultSlaDurationHours(s.getDefaultSlaDurationHours())
                .active(s.getActive())
                .displayOrder(s.getDisplayOrder())
                .build();
    }
}
