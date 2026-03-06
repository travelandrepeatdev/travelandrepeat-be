package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogController Tests")
class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    private UUID testBlogId;
    private BlogResponse testBlogResponse;
    private BlogRequest testBlogRequest;

    @BeforeEach
    void setUp() {
        testBlogId = UUID.randomUUID();

        testBlogResponse = BlogResponse.builder()
                .id(testBlogId)
                .title("Viajes Increíbles por Europa")
                .content("En este artículo te mostraremos los mejores destinos de Europa...")
                .slug("viajes-increibles-por-europa")
                .excerpt("excerpt")
                .status("Publicado")
                .coverImageUrl("coverImageUrl")
                .build();

        testBlogRequest = new BlogRequest(
                testBlogId,
                "Viajes Increíbles por Europa",
                "viajes-increibles-por-europa",
                "En este artículo te mostraremos los mejores destinos de Europa...",
                "excerpt",
                "coverImageUrl",
                "Publicado",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now()
                );
    }

    @Nested
    @DisplayName("getBlogList Tests")
    class GetBlogListTests {

        @Test
        @DisplayName("Should return list of all blogs")
        void shouldReturnListOfAllBlogs() {
            // Arrange
            List<BlogResponse> blogList = List.of(testBlogResponse);
            when(blogService.getBlogList()).thenReturn(blogList);

            // Act
            ResponseEntity<List<BlogResponse>> response = blogController.getBlogList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Viajes Increíbles por Europa", response.getBody().get(0).title());
            verify(blogService, times(1)).getBlogList();
        }

        @Test
        @DisplayName("Should return empty list when no blogs exist")
        void shouldReturnEmptyListWhenNoBlogsExist() {
            // Arrange
            when(blogService.getBlogList()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<BlogResponse>> response = blogController.getBlogList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(blogService, times(1)).getBlogList();
        }
    }

    @Nested
    @DisplayName("addBlog Tests")
    class AddBlogTests {

        @Test
        @DisplayName("Should add new blog successfully")
        void shouldAddNewBlogSuccessfully() {
            // Arrange
            BlogRequest newBlogRequest = BlogRequest.builder()
                    .title("Contenido del nuevo blog...")
                    .build();

            when(blogService.addBlog(any(BlogRequest.class), eq(false)))
                    .thenReturn(testBlogResponse);

            // Act
            ResponseEntity<BlogResponse> response = blogController.addBlog(newBlogRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Viajes Increíbles por Europa", response.getBody().title());
            verify(blogService, times(1)).addBlog(any(BlogRequest.class), eq(false));
        }

        @Test
        @DisplayName("Should return response entity with blog response")
        void shouldReturnResponseEntityWithBlogResponse() {
            // Arrange
            when(blogService.addBlog(any(BlogRequest.class), eq(false)))
                    .thenReturn(testBlogResponse);

            // Act
            ResponseEntity<BlogResponse> response = blogController.addBlog(testBlogRequest);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getBody());
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }
    }

    @Nested
    @DisplayName("deleteBlog Tests")
    class DeleteBlogTests {

        @Test
        @DisplayName("Should delete blog successfully")
        void shouldDeleteBlogSuccessfully() {
            // Arrange
            when(blogService.removeBlog(testBlogId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = blogController.deleteClient(testBlogId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody());
            verify(blogService, times(1)).removeBlog(testBlogId);
        }

        @Test
        @DisplayName("Should handle non-existent blog deletion")
        void shouldHandleNonExistentBlogDeletion() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(blogService.removeBlog(nonExistentId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = blogController.deleteClient(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(Boolean.TRUE, response.getBody());
            verify(blogService, times(1)).removeBlog(nonExistentId);
        }
    }

    @Nested
    @DisplayName("updateBlog Tests")
    class UpdateBlogTests {

        @Test
        @DisplayName("Should update existing blog successfully")
        void shouldUpdateExistingBlogSuccessfully() {
            // Arrange
            BlogRequest updateRequest = BlogRequest.builder()
                    .title("Blog Actualizado")
                    .build();

            BlogResponse updatedResponse = BlogResponse.builder()
                    .title("Blog Actualizado")
                    .build();

            when(blogService.modifyBlog(any(BlogRequest.class), eq(true)))
                    .thenReturn(updatedResponse);

            // Act
            ResponseEntity<BlogResponse> response = blogController.updateClient(updateRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Blog Actualizado", response.getBody().title());
            verify(blogService, times(1)).modifyBlog(any(BlogRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should return null when blog to update does not exist")
        void shouldReturnNullWhenBlogToUpdateDoesNotExist() {
            // Arrange
            when(blogService.modifyBlog(any(BlogRequest.class), eq(true)))
                    .thenReturn(null);

            // Act
            ResponseEntity<BlogResponse> response = blogController.updateClient(testBlogRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(blogService, times(1)).modifyBlog(any(BlogRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should toggle blog active status when updating")
        void shouldToggleBlogActiveStatusWhenUpdating() {
            // Arrange
            BlogRequest updateRequest = BlogRequest.builder()
                    .status("Borrador")
                    .build();

            BlogResponse updatedResponse = BlogResponse.builder()
                    .status("Borrador")
                    .build();

            when(blogService.modifyBlog(any(BlogRequest.class), eq(true)))
                    .thenReturn(updatedResponse);

            // Act
            ResponseEntity<BlogResponse> response = blogController.updateClient(updateRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Borrador", response.getBody().status());
            verify(blogService, times(1)).modifyBlog(any(BlogRequest.class), eq(true));
        }
    }
}


