package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.PromotionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepo promotionRepo;

    @Override
    public PromotionResponse addPromotion(PromotionRequest promotionRequest, boolean isUpdate) {
        PromotionResponse promotionResponse;
        Promotion promotion = mapRequestToEntity(promotionRequest, isUpdate);
        Promotion promotionEntity = promotionRepo.save(promotion);
        promotionResponse = mapEntityToResponse(promotionEntity);
        return promotionResponse;
    }

    private PromotionResponse mapEntityToResponse(Promotion promotionEntity) {
        return PromotionResponse.builder()
                .id(promotionEntity.getId())
                .originalPrice(promotionEntity.getOriginalPrice())
                .currency(promotionEntity.getCurrency())
                .description(promotionEntity.getDescription())
                .startDate(promotionEntity.getStartDate())
                .endDate(promotionEntity.getEndDate())
                .imageUrl(promotionEntity.getImageUrl())
                .promoPrice(promotionEntity.getPromoPrice())
                .isActive(promotionEntity.isActive())
                .title(promotionEntity.getTitle())
                .destination(promotionEntity.getDestination())
                .createdBy(promotionEntity.getCreatedBy())
                .build();
    }

    private Promotion mapRequestToEntity(PromotionRequest promotionRequest, boolean isUpdate) {
        return Promotion.builder()
                .id(isUpdate ? promotionRequest.id() : null)
                .endDate(promotionRequest.endDate() != null && !promotionRequest.endDate().isBlank() ?
                        LocalDate.parse(promotionRequest.endDate()).atStartOfDay() : null)
                .startDate(promotionRequest.startDate() != null && !promotionRequest.startDate().isBlank() ?
                        LocalDate.parse(promotionRequest.startDate()).atStartOfDay() : null)
                .description(promotionRequest.description())
                .imageUrl(promotionRequest.imageUrl())
                .isActive(promotionRequest.isActive())
                .originalPrice(promotionRequest.originalPrice())
                .currency(promotionRequest.currency())
                .title(promotionRequest.title())
                .destination(promotionRequest.destination())
                .createdBy(promotionRequest.createdBy())
                .updatedAt(LocalDateTime.now())
                .promoPrice(promotionRequest.promoPrice())
                .createdAt(isUpdate ? promotionRequest.createdAt() : LocalDateTime.now())
                .build();
    }

    @Override
    public boolean removeProvider(UUID promotionId) {
        promotionRepo.deleteById(promotionId);
        return true;
    }

    @Override
    public PromotionResponse modifyPromotion(PromotionRequest promotionRequest, boolean isUpdate) {
        Promotion promotion = promotionRepo.findById(promotionRequest.id()).orElse(null);
        if (promotion != null) {
            // keep created fields so needs new clientRequest
            return addPromotion(new PromotionRequest(
                    promotion.getId(),
                    promotionRequest.title(),
                    promotionRequest.description(),
                    promotionRequest.destination(),
                    promotionRequest.originalPrice(),
                    promotionRequest.promoPrice(),
                    promotionRequest.currency(),
                    promotionRequest.imageUrl(),
                    promotionRequest.startDate(),
                    promotionRequest.endDate(),
                    promotionRequest.isActive(),
                    promotion.getCreatedBy(),
                    promotionRequest.updatedAt(),
                    promotion.getCreatedAt()
            ), isUpdate);
        }
        return null;
    }

    @Override
    public List<PromotionResponse> getPromotionList() {
        List<PromotionResponse> promotionResponseList = new ArrayList<>();
        List<Promotion> promotionList = promotionRepo.findAll();
        promotionList.forEach(p -> promotionResponseList.add(
                PromotionResponse.builder()
                        .id(p.getId())
                        .endDate(p.getEndDate())
                        .startDate(p.getStartDate())
                        .description(p.getDescription())
                        .isActive(p.isActive())
                        .originalPrice(p.getOriginalPrice())
                        .currency(p.getCurrency())
                        .imageUrl(p.getImageUrl())
                        .promoPrice(p.getPromoPrice())
                        .title(p.getTitle())
                        .createdBy(p.getCreatedBy())
                        .destination(p.getDestination())
                        .updatedAt(p.getUpdatedAt())
                        .createdAt(p.getCreatedAt())
                        .build())
        );
        return promotionResponseList;
    }

    @Override
    public PromotionResponse enableDisable(UUID promotionId) {
        Promotion promotion = promotionRepo.findById(promotionId).orElse(null);
        if (promotion != null) {
            promotion.setActive(!promotion.isActive());
            Promotion promotionSaved =  promotionRepo.save(promotion);
            return mapEntityToResponse(promotionSaved);
        }
        return null;
    }

    @Override
    public List<PromotionResponse> getPromotionListActive() {
        return promotionRepo.findAll().stream()
                .filter(Promotion::isActive)
                .map(this::mapEntityToResponse)
                .toList();
    }
}
