package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepo extends JpaRepository<Client, UUID> {
}
