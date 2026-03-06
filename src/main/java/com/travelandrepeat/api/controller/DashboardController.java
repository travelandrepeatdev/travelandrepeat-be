package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.DashboardStats;
import com.travelandrepeat.api.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasAuthority('DASHBOARD_STATS')")
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
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
