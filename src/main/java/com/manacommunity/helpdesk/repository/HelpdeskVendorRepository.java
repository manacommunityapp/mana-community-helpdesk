package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskVendorRepository extends JpaRepository<HelpdeskVendor, Long> {
    List<HelpdeskVendor> findByCommunityIdAndActiveTrue(Long communityId);
    List<HelpdeskVendor> findByCommunityId(Long communityId);
}
