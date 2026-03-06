package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.service.BlogService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PreAuthorize("hasAuthority('BLOG_READ')")
    @GetMapping(path = "/blogList")
    public ResponseEntity<List<BlogResponse>> getBlogList() {
        return ResponseEntity.ok(blogService.getBlogList());
    }

    @PreAuthorize("hasAuthority('BLOG_CREATE')")
    @PostMapping(path = "/blog")
    public ResponseEntity<BlogResponse> addBlog(@RequestBody BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.addBlog(blogRequest, false));
    }

    @PreAuthorize("hasAuthority('BLOG_DELETE')")
    @DeleteMapping(path = "/blog")
    public ResponseEntity<Boolean> deleteClient(@PathParam("blogId") UUID blogId) {
        return ResponseEntity.ok(blogService.removeBlog(blogId));
    }

    @PreAuthorize("hasAuthority('BLOG_UPDATE')")
    @PutMapping(path = "/blog")
    public ResponseEntity<BlogResponse> updateClient(@RequestBody BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.modifyBlog(blogRequest, true));
    }
}
