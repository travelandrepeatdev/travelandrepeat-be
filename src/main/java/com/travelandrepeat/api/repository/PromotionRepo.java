package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionRepo extends JpaRepository<Promotion, UUID> {

}
