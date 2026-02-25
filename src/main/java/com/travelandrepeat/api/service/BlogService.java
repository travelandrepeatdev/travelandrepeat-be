package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    List<BlogResponse> getBlogList();
    BlogResponse addBlog(BlogRequest blogRequest, boolean isUpdate);
    boolean removeBlog(UUID blogId);
    BlogResponse modifyBlog(BlogRequest blogRequest, boolean isUpdate);
}
