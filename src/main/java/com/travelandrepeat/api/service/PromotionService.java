package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    PromotionResponse addPromotion(MultipartFile image, PromotionRequest promotionRequest, boolean isUpdate);
    String removePromotion(UUID promotionId);
    PromotionResponse modifyPromotion(MultipartFile image, PromotionRequest promotionRequest, boolean isUpdate);
    List<PromotionResponse> getPromotionList();
    PromotionResponse enableDisable(UUID promotionId);
    List<PromotionResponse> getPromotionListActive();
}
