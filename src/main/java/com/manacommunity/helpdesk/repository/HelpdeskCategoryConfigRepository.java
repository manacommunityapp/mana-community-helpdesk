package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.model.HelpdeskCategoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskCategoryConfigRepository extends JpaRepository<HelpdeskCategoryConfig, Long> {
    List<HelpdeskCategoryConfig> findByCommunityIdAndActiveTrueOrderByDisplayOrderAsc(Long communityId);
    Optional<HelpdeskCategoryConfig> findByCommunityIdAndCategory(Long communityId, HelpdeskCategory category);
}
