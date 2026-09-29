package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskCommentRepository extends JpaRepository<HelpdeskComment, Long> {
    List<HelpdeskComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
    List<HelpdeskComment> findByTicketIdAndInternalOnlyFalseOrderByCreatedAtAsc(Long ticketId);
}
