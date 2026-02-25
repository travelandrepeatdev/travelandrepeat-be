package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientResponse> findAll();
    ClientResponse addClient(ClientRequest clientRequest, boolean isUpdate);
    boolean removeClient(UUID clientId);
    ClientResponse modifyClient(ClientRequest clientRequest, boolean isUpdate);
}
