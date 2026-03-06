package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogRepo extends JpaRepository<Blog, UUID> {
}
