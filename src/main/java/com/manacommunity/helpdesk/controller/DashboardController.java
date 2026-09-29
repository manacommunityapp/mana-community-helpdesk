package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.AdminDashboardResponse;
import com.manacommunity.helpdesk.dto.ResidentDashboardResponse;
import com.manacommunity.helpdesk.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/helpdesk/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AnalyticsService analyticsService;

    @GetMapping("/admin")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(analyticsService.getAdminDashboard(communityId));
    }

    @GetMapping("/resident")
    public ResponseEntity<ResidentDashboardResponse> getResidentDashboard(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(analyticsService.getResidentDashboard(communityId, principal.getId()));
    }
}
