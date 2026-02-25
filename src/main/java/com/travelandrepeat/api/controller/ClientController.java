package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;
import com.travelandrepeat.api.service.ClientService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping(path = "/clientList")
    public ResponseEntity<List<ClientResponse>> getClientList() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    @PostMapping(path = "/client")
    public ResponseEntity<ClientResponse> addClient(@RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.addClient(clientRequest, false));
    }

    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @DeleteMapping(path = "/client")
    public ResponseEntity<Boolean> deleteClient(@PathParam("clientId") UUID clientId) {
        return ResponseEntity.ok(clientService.removeClient(clientId));
    }

    @PreAuthorize("hasAuthority('CLIENT_UPDATE')")
    @PutMapping(path = "/client")
    public ResponseEntity<?> updateClient(@RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.modifyClient(clientRequest, true));
    }
}
