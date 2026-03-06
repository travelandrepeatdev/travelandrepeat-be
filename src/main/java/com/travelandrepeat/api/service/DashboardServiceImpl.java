package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.DashboardStats;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.BlogRepo;
import com.travelandrepeat.api.repository.ClientRepo;
import com.travelandrepeat.api.repository.PromotionRepo;
import com.travelandrepeat.api.repository.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.travelandrepeat.api.service.BlogServiceImpl.PUBLISHED_STATUS;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ProviderRepo providerRepo;

    @Autowired
    private PromotionRepo promotionRepo;

    @Autowired
    private BlogRepo blogRepo;

    @Override
    public DashboardStats getDashboardStats() {

        long totalClients = clientRepo.count();
        long totalProviders = providerRepo.count();
        long activePromotions = promotionRepo.findAll().stream().filter(Promotion::isActive).count();
        long publishedBlogs = blogRepo.findAll().stream().filter(blog -> blog.getBlogStatus().equalsIgnoreCase(PUBLISHED_STATUS)).count();

        return DashboardStats.builder()
                .activePromotions(String.valueOf(activePromotions))
                .publishedBlogs(String.valueOf(publishedBlogs))
                .totalClients(String.valueOf(totalClients))
                .totalProviders(String.valueOf(totalProviders))
                .build();
    }
}
