package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.CategoryRequest;
import com.manacommunity.helpdesk.dto.CategoryResponse;
import com.manacommunity.helpdesk.dto.SubcategoryRequest;
import com.manacommunity.helpdesk.dto.SubcategoryResponse;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(categoryService.getCategories(communityId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(req, communityId));
    }

    @GetMapping("/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> getSubcategories(
            @RequestParam(required = false) HelpdeskCategory category,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(categoryService.getSubcategories(communityId, category));
    }

    @PostMapping("/subcategories")
    public ResponseEntity<SubcategoryResponse> createSubcategory(
            @Valid @RequestBody SubcategoryRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createSubcategory(req, communityId));
    }
}
