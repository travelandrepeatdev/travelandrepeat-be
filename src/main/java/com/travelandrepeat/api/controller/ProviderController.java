package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;
import com.travelandrepeat.api.service.ProviderService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PreAuthorize("hasAuthority('PROVIDER_READ')")
    @GetMapping(path = "/providerList")
    public ResponseEntity<List<ProviderResponse>> getProviderList() {
        return ResponseEntity.ok(providerService.getProviderList());
    }

    @PreAuthorize("hasAuthority('PROVIDER_CREATE')")
    @PostMapping(path = "/provider")
    public ResponseEntity<?> addProvider(@RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.ok(providerService.addProvider(providerRequest, false));
    }

    @PreAuthorize("hasAuthority('PROVIDER_DELETE')")
    @DeleteMapping(path = "/provider")
    public ResponseEntity<Boolean> deleteProvider(@PathParam("providerId") UUID providerId) {
        return ResponseEntity.ok(providerService.removeProvider(providerId));
    }

    @PreAuthorize("hasAuthority('PROVIDER_UPDATE')")
    @PutMapping(path = "/provider")
    public ResponseEntity<?> updateProvider(@RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.ok(providerService.modifyProvider(providerRequest, true));
    }
}
