package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;
import com.travelandrepeat.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientService clientService;

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping
    public List<ClientResponse> getClientList() {
        return clientService.findAll();
    }

    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    @PostMapping
    public ResponseEntity<ClientResponse> addClient(@RequestBody ClientRequest clientRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.addClient(clientRequest, false));
    }

    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.removeClient(id));
    }

    @PreAuthorize("hasAuthority('CLIENT_UPDATE')")
    @PutMapping
    public ResponseEntity<ClientResponse> updateClient(@RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.modifyClient(clientRequest, true));
    }
}
