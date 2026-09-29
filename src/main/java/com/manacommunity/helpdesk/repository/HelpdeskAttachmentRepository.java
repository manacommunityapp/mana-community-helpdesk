package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskAttachmentRepository extends JpaRepository<HelpdeskAttachment, Long> {
    List<HelpdeskAttachment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
