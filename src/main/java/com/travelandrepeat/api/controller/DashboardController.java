package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.DashboardStats;
import com.travelandrepeat.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasAuthority('DASHBOARD_STATS')")
    @GetMapping("/stats")
    public DashboardStats getDashboardStats() {
        return dashboardService.getDashboardStats();
    }

//    @PreAuthorize("hasAuthority('DASHBOARD_FINANCIAL')")
//    @GetMapping("/finacial")
//    public ResponseEntity<DashboardFinancial> getDashboardFinancial() {
//        return ResponseEntity.ok(dashboardService.getDashboardFinancial());
//    }
//
//    @PreAuthorize("hasAuthority('DASHBOARD_ACTIVITY')")
//    @GetMapping("/activity")
//    public ResponseEntity<DashboardActivity> getDashboardActivity() {
//        return ResponseEntity.ok(dashboardService.getDashboardActivity());
//    }
}
