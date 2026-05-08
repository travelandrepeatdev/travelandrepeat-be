package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/blogs")
public class BlogController {

    private final BlogService blogService;

    @PreAuthorize("hasAuthority('BLOG_READ')")
    @GetMapping
    public List<BlogResponse> getBlogList() {
        return blogService.getBlogList();
    }

    @PreAuthorize("hasAuthority('BLOG_CREATE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> addBlog(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "blogRequest") BlogRequest blogRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.addBlog(image, blogRequest, false));
    }

    @PreAuthorize("hasAuthority('BLOG_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID id) {
        return ResponseEntity.ok(blogService.removeBlog(id));
    }

    @PreAuthorize("hasAuthority('BLOG_UPDATE')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> updateClient(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "blogRequest") BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.modifyBlog(image, blogRequest, true));
    }

    @GetMapping(path = "/published")
    public List<BlogResponse> getPublishedBlogs() {
        return blogService.getBlogPublishedList();
    }

    @GetMapping(path = "/slug/{slug}")
    public BlogResponse getBlogBySlug(@PathVariable String slug) {
        return blogService.getBlogBySlug(slug);
    }
}
