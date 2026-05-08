package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.PromotionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepo promotionRepo;
    private final PromotionImageService promotionImageService;

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
        if (promotionEntity == null) {
            return null;
        }
        return PromotionResponse.builder()
                .id(promotionEntity.getId())
                .currency(promotionEntity.getCurrency())
                .description(promotionEntity.getDescription())
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
                .description(promotionRequest.getDescription())
                .isActive(promotionRequest.getIsActive())
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
            promotionImageService.remove(promotion.getImageUrl() == null ? "" : promotion.getImageUrl());
            promotionRepo.deleteById(promotionId);
            return promotion.getId().toString();
        } else {
            log.warn("Promotion {} not deleted because does not exist", promotionId);
        }
        return null;
    }

    @Override
    @Transactional
    public PromotionResponse modifyPromotion(MultipartFile image, PromotionRequest promotionRequest, boolean isUpdate) {
        Promotion promotion = promotionRepo.findById(promotionRequest.getId()).orElse(null);
        if (promotion == null) {
            return null;
        }
        promotionImageService.remove(promotion.getImageUrl() == null ? "" : promotion.getImageUrl());
        return addPromotion(image, new PromotionRequest(
                promotion.getId(),
                promotionRequest.getTitle(),
                promotionRequest.getDescription(),
                promotionRequest.getDestination(),
                promotionRequest.getPromoPrice(),
                promotionRequest.getCurrency(),
                promotionRequest.getIsActive(),
                promotion.getCreatedBy(),
                promotionRequest.getUpdatedAt(),
                promotion.getCreatedAt(),
                promotionRequest.getImageUrl()
            ), isUpdate);
    }

    @Override
    public List<PromotionResponse> getPromotionList() {
        List<PromotionResponse> promotionResponseList = new ArrayList<>();
        List<Promotion> promotionList = promotionRepo.findAll();
        promotionList.forEach(p -> promotionResponseList.add(
                PromotionResponse.builder()
                        .id(p.getId())
                        .description(p.getDescription())
                        .isActive(p.isActive())
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
        if (promotion == null) {
            return null;
        }
        promotion.setActive(!promotion.isActive());
        Promotion promotionSaved =  promotionRepo.save(promotion);
        return mapEntityToResponse(promotionSaved);
    }

    @Override
    public List<PromotionResponse> getPromotionListActive() {
        return promotionRepo.findAll().stream()
                .filter(Promotion::isActive)
                .map(this::mapEntityToResponse)
                .toList();
    }
}
