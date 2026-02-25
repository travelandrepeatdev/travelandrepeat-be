package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepo extends JpaRepository<Provider, UUID> {
}
