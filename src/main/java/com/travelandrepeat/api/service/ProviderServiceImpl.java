package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;
import com.travelandrepeat.api.entity.Provider;
import com.travelandrepeat.api.repository.ProviderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepo providerRepo;

    @Override
    public List<ProviderResponse> getProviderList() {
        List<ProviderResponse> providerResponseList = new ArrayList<>();
        List<Provider> providerList = providerRepo.findAll();
        providerList.forEach(p -> providerResponseList.add(
                ProviderResponse.builder()
                        .id(p.getId())
                        .email(p.getEmail())
                        .name(p.getName())
                        .notes(p.getNotes())
                        .phone(p.getPhone())
                        .category(p.getCategory())
                        .contactName(p.getContactName())
                        .website(p.getWebsite())
                        .build())
        );
        return providerResponseList;
    }

    @Override
    public ProviderResponse addProvider(ProviderRequest providerRequest, boolean isUpdate) {
        Provider provider = mapRequestToEntity(providerRequest, isUpdate);
        Provider providerEntity = providerRepo.save(provider);
        return mapEntityToResponse(providerEntity);
    }

    private ProviderResponse mapEntityToResponse(Provider providerEntity) {
        if (providerEntity == null) {
            return null;
        }
        return ProviderResponse.builder()
                .id(providerEntity.getId())
                .email(providerEntity.getEmail())
                .name(providerEntity.getName())
                .notes(providerEntity.getNotes())
                .phone(providerEntity.getPhone())
                .category(providerEntity.getCategory())
                .website(providerEntity.getWebsite())
                .contactName(providerEntity.getContactName())
                .build();
    }

    private Provider mapRequestToEntity(ProviderRequest providerRequest, boolean isUpdate) {
        return Provider.builder()
                .id(isUpdate ? providerRequest.id() : null)
                .email(providerRequest.email())
                .name(providerRequest.name())
                .notes(providerRequest.notes())
                .phone(providerRequest.phone())
                .category(providerRequest.category())
                .website(providerRequest.website())
                .contactName(providerRequest.contactName())
                .createdAt(isUpdate ? providerRequest.createdAt() : LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(providerRequest.createdBy())
                .build();
    }

    @Override
    public String removeProvider(UUID providerId) {
        Provider provider = providerRepo.findById(providerId).orElse(null);
        if (provider != null) {
            providerRepo.deleteById(providerId);
            return provider.getId().toString();
        } else {
            log.warn("Provider {} not deleted because does not exist", providerId);
        }
        return null;
    }

    @Override
    public ProviderResponse modifyProvider(ProviderRequest providerRequest, boolean isUpdate) {
        Provider provider = providerRepo.findById(providerRequest.id()).orElse(null);
        if (provider == null) {
            return null;
        }
        return addProvider(new ProviderRequest(
                providerRequest.id(),
                providerRequest.name(),
                providerRequest.contactName(),
                providerRequest.email(),
                providerRequest.phone(),
                providerRequest.category(),
                providerRequest.website(),
                providerRequest.notes(),
                provider.getCreatedBy(),
                provider.getCreatedAt(),
                null // updatedAt Changed on save with isUpdate = true
        ), isUpdate);
    }
}
