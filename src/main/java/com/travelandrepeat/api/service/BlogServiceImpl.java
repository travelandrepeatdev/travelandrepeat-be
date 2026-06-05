package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.entity.Blog;
import com.travelandrepeat.api.repository.BlogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepo blogRepo;
    private final PromotionImageService promotionImageService;

    public static final String PUBLISHED_STATUS = "Publicado";

    @Override
    public List<BlogResponse> getBlogList() {
        List<BlogResponse> blogResponseList = new ArrayList<>();
        List<Blog> blogList = blogRepo.findAll();
        blogList.forEach(blog -> blogResponseList.add(
                BlogResponse.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .slug(blog.getSlug())
                        .createdBy(blog.getCreatedBy())
                        .content(blog.getContent())
                        .excerpt(blog.getExcerpt())
                        .coverImageUrl(blog.getCoverImageUrl())
                        .status(blog.getBlogStatus())
                        .build())
        );
        return blogResponseList;
    }

    @Override
    public BlogResponse addBlog(MultipartFile image, BlogRequest blogRequest, boolean isUpdate) {
        Blog blog = mapRequestToEntity(blogRequest, isUpdate);

        String imageUrl = promotionImageService.save(image);
        blog.setCoverImageUrl(image != null && !image.isEmpty() ? imageUrl : blogRequest.coverImageUrl());

        Blog blogEntity = blogRepo.save(blog);
        return mapEntityToResponse(blogEntity);
    }

    @Override
    public String removeBlog(UUID blogId) {
        Blog blog = blogRepo.findById(blogId).orElse(null);
        if (blog != null) {
            promotionImageService.remove(blog.getCoverImageUrl() == null ? "" : blog.getCoverImageUrl());
            blogRepo.deleteById(blogId);
            return blog.getId().toString();
        } else {
            log.warn("Blog {} not deleted because does not exist", blogId);
        }
        return null;
    }

    @Override
    public BlogResponse modifyBlog(MultipartFile image, BlogRequest blogRequest, boolean isUpdate) {
        Blog blog = blogRepo.findById(blogRequest.id()).orElse(null);
        if (blog == null) {
            return null;
        }
        if (image != null && !image.isEmpty()) {
            promotionImageService.remove(blog.getCoverImageUrl() == null ? "" : blog.getCoverImageUrl());
        }
        return addBlog(image, new BlogRequest(
                blogRequest.id(),
                blogRequest.title(),
                blogRequest.slug(),
                blogRequest.content(),
                blogRequest.excerpt(),
                blogRequest.coverImageUrl(),
                blogRequest.status(),
                blogRequest.status().equalsIgnoreCase(PUBLISHED_STATUS) ? LocalDateTime.now() : null,
                blog.getCreatedBy(),
                blog.getCreatedAt(),
                null // updatedAt Changed on save with isUpdate = true
        ), isUpdate);
    }

    @Override
    public List<BlogResponse> getBlogPublishedList() {
        return getBlogList().stream()
                .filter(b -> b.status().equalsIgnoreCase(PUBLISHED_STATUS))
                .toList();
    }

    @Override
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepo.findBySlug(slug);
        return mapEntityToResponse(blog);
    }

    private BlogResponse mapEntityToResponse(Blog blogEntity) {
        if (blogEntity == null) {
            return null;
        }
        return BlogResponse.builder()
                .slug(blogEntity.getSlug())
                .title(blogEntity.getTitle())
                .id(blogEntity.getId())
                .excerpt(blogEntity.getExcerpt())
                .coverImageUrl(blogEntity.getCoverImageUrl())
                .status(blogEntity.getBlogStatus())
                .content(blogEntity.getContent())
                .createdBy(blogEntity.getCreatedBy())
                .build();
    }

    private Blog mapRequestToEntity(BlogRequest blogRequest, boolean isUpdate) {
        return Blog.builder()
                .blogStatus(blogRequest.status())
                .title(blogRequest.title())
                .slug(blogRequest.slug())
                .createdBy(blogRequest.createdBy())
                .content(blogRequest.content())
                .excerpt(blogRequest.excerpt())
                .updatedAt(LocalDateTime.now())
                .createdAt(isUpdate ? blogRequest.createdAt() : LocalDateTime.now())
                .id(isUpdate ? blogRequest.id() : null)
                .coverImageUrl(blogRequest.coverImageUrl())
                .publishedAt(blogRequest.status().equalsIgnoreCase(PUBLISHED_STATUS) ? blogRequest.publishedAt() : null)
                .build();
    }
}
