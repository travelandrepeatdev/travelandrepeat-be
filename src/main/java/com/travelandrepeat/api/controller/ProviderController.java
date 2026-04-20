package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;
import com.travelandrepeat.api.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/providers")
public class ProviderController {

    private final ProviderService providerService;

    @PreAuthorize("hasAuthority('PROVIDER_READ')")
    @GetMapping
    public List<ProviderResponse> getProviderList() {
        return providerService.getProviderList();
    }

    @PreAuthorize("hasAuthority('PROVIDER_CREATE')")
    @PostMapping
    public ResponseEntity<ProviderResponse> addProvider(@RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.addProvider(providerRequest, false));
    }

    @PreAuthorize("hasAuthority('PROVIDER_DELETE')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable UUID id) {
        return ResponseEntity.ok(providerService.removeProvider(id));
    }

    @PreAuthorize("hasAuthority('PROVIDER_UPDATE')")
    @PutMapping
    public ResponseEntity<ProviderResponse> updateProvider(@RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.ok(providerService.modifyProvider(providerRequest, true));
    }
}
