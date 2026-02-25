package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.service.PromotionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PreAuthorize("hasAuthority('PROMOTION_READ')")
    @GetMapping(path = "/promotionList")
    public ResponseEntity<List<PromotionResponse>> getPromotionList() {
        return ResponseEntity.ok(promotionService.getPromotionList());
    }

    @GetMapping(path = "/promotionListActive")
    public ResponseEntity<List<PromotionResponse>> getPromotionListActive() {
        return ResponseEntity.ok(promotionService.getPromotionListActive());
    }

    @PreAuthorize("hasAuthority('PROMOTION_CREATE')")
    @PostMapping(path = "/promotion")
    public ResponseEntity<PromotionResponse> addPromotion(@RequestBody PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.addPromotion(promotionRequest, false));
    }

    @PreAuthorize("hasAuthority('PROMOTION_DELETE')")
    @DeleteMapping(path = "/promotion")
    public ResponseEntity<Boolean> deletePromotion(@PathParam("promotionId") UUID promotionId) {
        return ResponseEntity.ok(promotionService.removeProvider(promotionId));
    }

    @PreAuthorize("hasAuthority('PROMOTION_UPDATE')")
    @PutMapping(path = "/promotion")
    public ResponseEntity<?> updatePromotion(@RequestBody PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.modifyPromotion(promotionRequest, true));
    }

    @PreAuthorize("hasAuthority('PROMOTION_ENABLE_DISABLE')")
    @PutMapping(path = "/promotionEnableDisable")
    public ResponseEntity<PromotionResponse> enableDisablePromotion(@PathParam("promotionId") UUID promotionId) {
        return ResponseEntity.ok(promotionService.enableDisable(promotionId));
    }


}
