package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    PromotionResponse addPromotion(PromotionRequest promotionRequest, boolean isUpdate);
    boolean removeProvider(UUID promotionId);
    PromotionResponse modifyPromotion(PromotionRequest promotionRequest, boolean isUpdate);
    List<PromotionResponse> getPromotionList();
    PromotionResponse enableDisable(UUID promotionId);
    List<PromotionResponse> getPromotionListActive();
}
