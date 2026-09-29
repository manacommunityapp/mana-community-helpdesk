package com.manacommunity.helpdesk.controller;

import com.manacommunity.helpdesk.dto.VisitRequest;
import com.manacommunity.helpdesk.dto.VisitResponse;
import com.manacommunity.helpdesk.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/tickets/{ticketId}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<VisitResponse>> getVisits(@PathVariable Long ticketId) {
        return ResponseEntity.ok(vendorService.getVisits(ticketId));
    }

    @PostMapping
    public ResponseEntity<VisitResponse> recordVisit(
            @PathVariable Long ticketId,
            @Valid @RequestBody VisitRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.recordVisit(ticketId, req));
    }
}
