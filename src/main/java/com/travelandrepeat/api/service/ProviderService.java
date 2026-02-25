package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;

import java.util.List;
import java.util.UUID;

public interface ProviderService {
    List<ProviderResponse> getProviderList();
    ProviderResponse addProvider(ProviderRequest providerRequest, boolean isUpdate);
    boolean removeProvider(UUID providerId);
    ProviderResponse modifyProvider(ProviderRequest providerRequest, boolean isUpdate);
}
