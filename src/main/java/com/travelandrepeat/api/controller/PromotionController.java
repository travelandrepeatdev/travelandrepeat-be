package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PreAuthorize("hasAuthority('PROMOTION_READ')")
    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getPromotionList() {
        return ResponseEntity.ok(promotionService.getPromotionList());
    }

    @GetMapping(path = "/promotionListActive")
    public ResponseEntity<List<PromotionResponse>> getPromotionListActive() {
        return ResponseEntity.ok(promotionService.getPromotionListActive());
    }

    @PreAuthorize("hasAuthority('PROMOTION_CREATE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PromotionResponse> addPromotion(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "promotionRequest") PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.addPromotion(image, promotionRequest, false));
    }

    @PreAuthorize("hasAuthority('PROMOTION_DELETE')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deletePromotion(@PathVariable UUID id) {
        return ResponseEntity.ok(promotionService.removePromotion(id));
    }

    @PreAuthorize("hasAuthority('PROMOTION_UPDATE')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PromotionResponse> updatePromotion(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "promotionRequest") PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.modifyPromotion(image, promotionRequest, true));
    }

    @PreAuthorize("hasAuthority('PROMOTION_ENABLE_DISABLE')")
    @PutMapping(path = "/{id}/enable-disable")
    public ResponseEntity<PromotionResponse> enableDisablePromotion(@PathVariable UUID id) {
        return ResponseEntity.ok(promotionService.enableDisable(id));
    }


}
