package com.travelandrepeat.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PromotionRequest {
    private UUID id;
    private String title;
    private String description;
    private String destination;
    private BigDecimal originalPrice;
    private BigDecimal promoPrice;
    private String currency;
    private String startDate;
    private String endDate;
    private Boolean isActive;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String imageUrl;
}

