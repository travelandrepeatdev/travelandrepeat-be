package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    List<BlogResponse> getBlogList();
    BlogResponse addBlog(MultipartFile image, BlogRequest blogRequest, boolean isUpdate);
    String removeBlog(UUID blogId);
    BlogResponse modifyBlog(MultipartFile image, BlogRequest blogRequest, boolean isUpdate);
    List<BlogResponse> getBlogPublishedList();
    BlogResponse getBlogBySlug(String slug);
}
