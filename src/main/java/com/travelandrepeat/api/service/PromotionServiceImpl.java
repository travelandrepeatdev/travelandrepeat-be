package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.PromotionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    @Autowired
    private PromotionImageService promotionImageService;

    @Override
    @Transactional
    public PromotionResponse addPromotion(MultipartFile image, PromotionRequest promotionRequest, boolean isUpdate) {
        PromotionResponse promotionResponse;
        Promotion promotion = mapRequestToEntity(promotionRequest, isUpdate);

        String imageUrl = promotionImageService.save(image);
        promotion.setImageUrl(imageUrl);

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
                .id(isUpdate ? promotionRequest.getId() : null)
                .endDate(promotionRequest.getEndDate() != null && !promotionRequest.getEndDate().isBlank() ?
                        LocalDate.parse(promotionRequest.getEndDate()).atStartOfDay() : null)
                .startDate(promotionRequest.getStartDate() != null && !promotionRequest.getStartDate().isBlank() ?
                        LocalDate.parse(promotionRequest.getStartDate()).atStartOfDay() : null)
                .description(promotionRequest.getDescription())
                .isActive(promotionRequest.getIsActive())
                .originalPrice(promotionRequest.getOriginalPrice())
                .currency(promotionRequest.getCurrency())
                .title(promotionRequest.getTitle())
                .destination(promotionRequest.getDestination())
                .createdBy(promotionRequest.getCreatedBy())
                .updatedAt(LocalDateTime.now())
                .promoPrice(promotionRequest.getPromoPrice())
                .createdAt(isUpdate ? promotionRequest.getCreatedAt() : LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public String removePromotion(UUID promotionId) {
        Promotion promotion = promotionRepo.findById(promotionId).orElse(null);
        if (promotion != null) {
            promotionImageService.remove(promotion.getImageUrl());
            promotionRepo.deleteById(promotionId);
            return promotionId.toString();
        } else {
            log.warn("Promotion with id {} not found", promotionId);
        }
        return null;
    }

    @Override
    @Transactional
    public PromotionResponse modifyPromotion(MultipartFile image, PromotionRequest promotionRequest, boolean isUpdate) {
        Promotion promotion = promotionRepo.findById(promotionRequest.getId()).orElse(null);
        if (promotion != null) {
            promotionImageService.remove(promotion.getImageUrl());

            return addPromotion(image, new PromotionRequest(
                    promotion.getId(),
                    promotionRequest.getTitle(),
                    promotionRequest.getDescription(),
                    promotionRequest.getDestination(),
                    promotionRequest.getOriginalPrice(),
                    promotionRequest.getPromoPrice(),
                    promotionRequest.getCurrency(),
                    promotionRequest.getStartDate(),
                    promotionRequest.getEndDate(),
                    promotionRequest.getIsActive(),
                    promotion.getCreatedBy(),
                    promotionRequest.getUpdatedAt(),
                    promotion.getCreatedAt(),
                    promotionRequest.getImageUrl()
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
    @Transactional
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
