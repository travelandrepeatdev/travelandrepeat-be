package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public ResponseEntity<BlogResponse> addBlog(@RequestBody BlogRequest blogRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.addBlog(blogRequest, false));
    }

    @PreAuthorize("hasAuthority('BLOG_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID id) {
        return ResponseEntity.ok(blogService.removeBlog(id));
    }

    @PreAuthorize("hasAuthority('BLOG_UPDATE')")
    @PutMapping
    public ResponseEntity<BlogResponse> updateClient(@RequestBody BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.modifyBlog(blogRequest, true));
    }
}
