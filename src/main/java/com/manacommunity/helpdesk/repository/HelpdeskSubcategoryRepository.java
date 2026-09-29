package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.model.HelpdeskSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskSubcategoryRepository extends JpaRepository<HelpdeskSubcategory, Long> {
    List<HelpdeskSubcategory> findByCommunityIdAndCategoryAndActiveTrueOrderByDisplayOrderAsc(Long communityId, HelpdeskCategory category);
    List<HelpdeskSubcategory> findByCommunityIdAndActiveTrue(Long communityId);
    Optional<HelpdeskSubcategory> findByCommunityIdAndCode(Long communityId, String code);
}
