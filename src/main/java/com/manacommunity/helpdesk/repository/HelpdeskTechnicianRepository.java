package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskTechnicianRepository extends JpaRepository<HelpdeskTechnician, Long> {
    List<HelpdeskTechnician> findByVendorIdAndActiveTrue(Long vendorId);
    List<HelpdeskTechnician> findByCommunityIdAndActiveTrue(Long communityId);
    Optional<HelpdeskTechnician> findFirstByVendorIdAndActiveTrueOrderByCurrentWorkloadAsc(Long vendorId);
}
