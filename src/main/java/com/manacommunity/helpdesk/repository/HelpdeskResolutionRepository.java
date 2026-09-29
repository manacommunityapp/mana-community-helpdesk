package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HelpdeskResolutionRepository extends JpaRepository<HelpdeskResolution, Long> {
    Optional<HelpdeskResolution> findByTicketId(Long ticketId);
}
