package com.travelandrepeat.api.dto;

import lombok.Builder;

@Builder
public record DashboardStats(
        String totalClients,
        String totalProviders,
        String activePromotions,
        String publishedBlogs
) {
}
