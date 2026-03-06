package com.travelandrepeat.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String excerpt;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = false, name = "status")
    private String blogStatus;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false, name = "created_by")
    private UUID createdBy;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
