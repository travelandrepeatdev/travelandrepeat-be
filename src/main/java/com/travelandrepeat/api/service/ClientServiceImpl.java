package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;
import com.travelandrepeat.api.entity.Client;
import com.travelandrepeat.api.repository.ClientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public List<ClientResponse> findAll() {
        List<ClientResponse> clientResponseList = new ArrayList<>();
        List<Client> clientList = clientRepo.findAll();
        clientList.forEach(client -> clientResponseList.add(
                ClientResponse.builder()
                        .notes(client.getNotes())
                        .name(client.getName())
                        .email(client.getEmail())
                        .phone(client.getPhone())
                        .updatedAt(client.getUpdatedAt().toString())
                        .address(client.getAddress())
                        .countryCode(client.getCountryCode())
                        .id(client.getId().toString())
                        .createdAt(client.getCreatedAt().toString())
                        .createdBy(client.getCreatedBy().toString())
                        .build())
        );
        return clientResponseList;
    }

    @Override
    public ClientResponse addClient(ClientRequest clientRequest, boolean isUpdate) {
        ClientResponse clientResponse;
        Client client = mapRequestToEntity(clientRequest, isUpdate);
        Client clientEntity = clientRepo.save(client);
        clientResponse = mapEntityToResponse(clientEntity);
        return clientResponse;
    }

    @Override
    public boolean removeClient(UUID clientId) {
        clientRepo.deleteById(clientId);
        return true;
    }

    @Override
    public ClientResponse modifyClient(ClientRequest clientRequest, boolean isUpdate) {
        Client client = clientRepo.findById(clientRequest.id()).orElse(null);
        if (client != null) {
            // keep created fields so needs new clientRequest
            return addClient(new ClientRequest(
                    client.getId(),
                    clientRequest.name(),
                    clientRequest.email(),
                    clientRequest.phone(),
                    clientRequest.countryCode(),
                    clientRequest.address(),
                    clientRequest.notes(),
                    client.getCreatedBy(),
                    client.getCreatedAt(),
                    null // updatedAt Changed on save with isUpdate = true
            ), isUpdate);
        }
        return null;
    }

    private ClientResponse mapEntityToResponse(Client clientEntity) {
        return ClientResponse.builder()
                .createdAt(clientEntity.getCreatedAt().toString())
                .updatedAt(clientEntity.getUpdatedAt().toString())
                .createdBy(clientEntity.getCreatedBy().toString())
                .email(clientEntity.getEmail())
                .name(clientEntity.getName())
                .notes(clientEntity.getNotes())
                .phone(clientEntity.getPhone())
                .address(clientEntity.getAddress())
                .countryCode(clientEntity.getCountryCode())
                .id(clientEntity.getId().toString())
                .build();
    }

    private Client mapRequestToEntity(ClientRequest clientRequest, boolean isUpdate) {
        return Client.builder()
                .id(isUpdate ? clientRequest.id() : null)
                .email(clientRequest.email())
                .notes(clientRequest.notes())
                .phone(clientRequest.phone())
                .createdAt(isUpdate ? clientRequest.createdAt() : LocalDateTime.now())
                .createdBy(clientRequest.createdBy())
                .countryCode(clientRequest.countryCode())
                .address(clientRequest.address())
                .name(clientRequest.name())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
