package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.BlogRequest;
import com.travelandrepeat.api.dto.BlogResponse;
import com.travelandrepeat.api.entity.Blog;
import com.travelandrepeat.api.repository.BlogRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogService Tests")
class BlogServiceTest {

    @Mock
    private BlogRepo blogRepo;

    @InjectMocks
    private BlogServiceImpl blogService;

    private UUID testBlogId;
    private UUID testUserId;
    private Blog testBlog;
    private BlogRequest testBlogRequest;

    @BeforeEach
    void setUp() {
        testBlogId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testBlog = new Blog();
        testBlog.setId(testBlogId);
        testBlog.setTitle("Viajes Increíbles por Europa");
        testBlog.setContent("En este artículo te mostraremos los mejores destinos de Europa...");
        testBlog.setBlogStatus("Borrador");
        testBlog.setCreatedBy(testUserId);
        testBlog.setCreatedAt(LocalDateTime.now());
        testBlog.setUpdatedAt(LocalDateTime.now());

        testBlogRequest = new BlogRequest(
                testBlogId,
                "Viajes Increíbles por Europa",
                "viajes-increibles-por-europa",
                "En este artículo te mostraremos los mejores destinos de Europa...",
                "excerpt",
                "coverImageUrl",
                "Borrador",
                LocalDateTime.now(),
                testUserId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("getBlogList Tests")
    class GetBlogListTests {

        @Test
        @DisplayName("Should return list of all blogs as BlogResponse")
        void shouldReturnListOfAllBlogsAsBlogResponse() {
            // Arrange
            Blog blog2 = new Blog();
            blog2.setId(UUID.randomUUID());
            blog2.setTitle("Consejos para Viajeros");
            blog2.setContent("Tips útiles para tus viajes...");
            blog2.setCreatedBy(testUserId);
            blog2.setCreatedAt(LocalDateTime.now());
            blog2.setUpdatedAt(LocalDateTime.now());

            List<Blog> blogList = List.of(testBlog, blog2);
            when(blogRepo.findAll()).thenReturn(blogList);

            // Act
            List<BlogResponse> result = blogService.getBlogList();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Viajes Increíbles por Europa", result.get(0).title());
            assertEquals("Consejos para Viajeros", result.get(1).title());
            verify(blogRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no blogs exist")
        void shouldReturnEmptyListWhenNoBlogsExist() {
            // Arrange
            when(blogRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<BlogResponse> result = blogService.getBlogList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(blogRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should properly map blog entity to response DTO")
        void shouldProperlyMapBlogEntityToResponseDTO() {
            // Arrange
            when(blogRepo.findAll()).thenReturn(List.of(testBlog));

            // Act
            List<BlogResponse> result = blogService.getBlogList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            BlogResponse response = result.get(0);
            assertEquals("Viajes Increíbles por Europa", response.title());
            assertEquals("Borrador", response.status());
            assertNotNull(response.createdBy());
        }
    }

    @Nested
    @DisplayName("addBlog Tests")
    class AddBlogTests {

        @Test
        @DisplayName("Should add new blog successfully")
        void shouldAddNewBlogSuccessfully() {
            // Arrange
            BlogRequest newBlogRequest = new BlogRequest(
                    null,
                    "Nuevo Blog",
                    "nuevo-blog",
                    "Contenido del nuevo blog...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            Blog savedBlog = new Blog();
            savedBlog.setId(UUID.randomUUID());
            savedBlog.setTitle("Nuevo Blog");
            savedBlog.setContent("Contenido del nuevo blog...");
            savedBlog.setBlogStatus("Publicado");
            savedBlog.setCreatedBy(testUserId);
            savedBlog.setCreatedAt(LocalDateTime.now());
            savedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.save(any(Blog.class))).thenReturn(savedBlog);

            // Act
            BlogResponse result = blogService.addBlog(newBlogRequest, false);

            // Assert
            assertNotNull(result);
            assertEquals("Nuevo Blog", result.title());
            assertEquals("Publicado", result.status());
            verify(blogRepo, times(1)).save(any(Blog.class));
        }

        @Test
        @DisplayName("Should set id to null when adding new blog (isUpdate = false)")
        void shouldSetIdToNullWhenAddingNewBlog() {
            // Arrange
            BlogRequest newBlogRequest = new BlogRequest(
                    UUID.randomUUID(),
                    "Nuevo Blog",
                    "nuevo-blog",
                    "Contenido...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    testUserId,
                    null,
                    null
            );

            Blog savedBlog = new Blog();
            savedBlog.setId(UUID.randomUUID());
            savedBlog.setTitle("Nuevo Blog");
            savedBlog.setCreatedAt(LocalDateTime.now());
            savedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.save(any(Blog.class))).thenReturn(savedBlog);

            // Act
            blogService.addBlog(newBlogRequest, false);

            // Assert
            verify(blogRepo, times(1)).save(argThat(blog -> blog.getId() == null));
        }

        @Test
        @DisplayName("Should set createdAt timestamp when adding new blog")
        void shouldSetCreatedAtTimestampWhenAddingNewBlog() {
            // Arrange
            BlogRequest newBlogRequest = new BlogRequest(
                    UUID.randomUUID(),
                    "Nuevo Blog",
                    "nuevo-blog",
                    "Contenido...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    testUserId,
                    null,
                    null
            );

            Blog savedBlog = new Blog();
            savedBlog.setId(UUID.randomUUID());
            savedBlog.setTitle("Nuevo Blog");
            savedBlog.setCreatedAt(LocalDateTime.now());
            savedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.save(any(Blog.class))).thenReturn(savedBlog);

            // Act
            blogService.addBlog(newBlogRequest, false);

            // Assert
            verify(blogRepo, times(1)).save(argThat(blog -> blog.getCreatedAt() != null));
        }

        @Test
        @DisplayName("Should keep id when updating blog (isUpdate = true)")
        void shouldKeepIdWhenUpdatingBlog() {
            // Arrange
            BlogRequest updateBlogRequest = new BlogRequest(
                    testBlogId,
                    "Blog Actualizado",
                    "blog-actualizado",
                    "Contenido actualizado...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            Blog savedBlog = new Blog();
            savedBlog.setId(testBlogId);
            savedBlog.setTitle("Blog Actualizado");
            savedBlog.setCreatedAt(testBlog.getCreatedAt());
            savedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.save(any(Blog.class))).thenReturn(savedBlog);

            // Act
            blogService.addBlog(updateBlogRequest, true);

            // Assert
            verify(blogRepo, times(1)).save(argThat(blog -> testBlogId.equals(blog.getId())));
        }
    }

    @Nested
    @DisplayName("removeBlog Tests")
    class RemoveBlogTests {

        @Test
        @DisplayName("Should delete blog successfully")
        void shouldDeleteBlogSuccessfully() {
            // Arrange
            doNothing().when(blogRepo).deleteById(testBlogId);

            // Act
            boolean result = blogService.removeBlog(testBlogId);

            // Assert
            assertTrue(result);
            verify(blogRepo, times(1)).deleteById(testBlogId);
        }

        @Test
        @DisplayName("Should handle deletion of non-existent blog")
        void shouldHandleDeletionOfNonExistentBlog() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            doNothing().when(blogRepo).deleteById(nonExistentId);

            // Act
            boolean result = blogService.removeBlog(nonExistentId);

            // Assert
            assertTrue(result);
            verify(blogRepo, times(1)).deleteById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("modifyBlog Tests")
    class ModifyBlogTests {

        @Test
        @DisplayName("Should update existing blog successfully")
        void shouldUpdateExistingBlogSuccessfully() {
            // Arrange
            BlogRequest updateBlogRequest = new BlogRequest(
                    testBlogId,
                    "Blog Actualizado",
                    "blog-actualizado",
                    "Contenido actualizado...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            Blog updatedBlog = new Blog();
            updatedBlog.setId(testBlogId);
            updatedBlog.setTitle("Blog Actualizado");
            updatedBlog.setContent("Contenido actualizado...");
            updatedBlog.setBlogStatus("Publicado");
            updatedBlog.setCreatedAt(testBlog.getCreatedAt());
            updatedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.findById(testBlogId)).thenReturn(Optional.of(testBlog));
            when(blogRepo.save(any(Blog.class))).thenReturn(updatedBlog);

            // Act
            BlogResponse result = blogService.modifyBlog(updateBlogRequest, true);

            // Assert
            assertNotNull(result);
            assertEquals("Blog Actualizado", result.title());
            assertEquals("Contenido actualizado...", result.content());
            verify(blogRepo, times(1)).findById(testBlogId);
            verify(blogRepo, times(1)).save(any(Blog.class));
        }

        @Test
        @DisplayName("Should return null when blog to update does not exist")
        void shouldReturnNullWhenBlogToUpdateDoesNotExist() {
            // Arrange
            BlogRequest updateBlogRequest = new BlogRequest(
                    testBlogId,
                    "Blog Actualizado",
                    "blog-actualizado",
                    "Contenido actualizado...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            when(blogRepo.findById(testBlogId)).thenReturn(Optional.empty());

            // Act
            BlogResponse result = blogService.modifyBlog(updateBlogRequest, true);

            // Assert
            assertNull(result);
            verify(blogRepo, times(1)).findById(testBlogId);
            verify(blogRepo, never()).save(any(Blog.class));
        }

        @Test
        @DisplayName("Should preserve createdAt and createdBy when updating")
        void shouldPreserveCreatedAtAndCreatedByWhenUpdating() {
            // Arrange
            BlogRequest updateBlogRequest = new BlogRequest(
                    testBlogId,
                    "Blog Actualizado",
                    "blog-actualizado",
                    "Contenido actualizado...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            Blog updatedBlog = new Blog();
            updatedBlog.setId(testBlogId);
            updatedBlog.setCreatedAt(testBlog.getCreatedAt());
            updatedBlog.setCreatedBy(testBlog.getCreatedBy());
            updatedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.findById(testBlogId)).thenReturn(Optional.of(testBlog));
            when(blogRepo.save(any(Blog.class))).thenReturn(updatedBlog);

            // Act
            blogService.modifyBlog(updateBlogRequest, true);

            // Assert
            verify(blogRepo).save(argThat(blog ->
                    testBlog.getCreatedAt().equals(blog.getCreatedAt()) &&
                    testBlog.getCreatedBy().equals(blog.getCreatedBy())
            ));
        }

        @Test
        @DisplayName("Should toggle blog active status")
        void shouldToggleBlogActiveStatus() {
            // Arrange
            testBlog.setBlogStatus("Publicado");
            BlogRequest updateBlogRequest = new BlogRequest(
                    testBlogId,
                    "Blog Actualizado",
                    "blog-actualizado",
                    "Contenido actualizado...",
                    "excerpt",
                    "coverImageUrl",
                    "Publicado",
                    null,
                    null,
                    null,
                    null
            );

            Blog updatedBlog = new Blog();
            updatedBlog.setId(testBlogId);
            updatedBlog.setBlogStatus("Publicado");
            updatedBlog.setCreatedAt(testBlog.getCreatedAt());
            updatedBlog.setUpdatedAt(LocalDateTime.now());

            when(blogRepo.findById(testBlogId)).thenReturn(Optional.of(testBlog));
            when(blogRepo.save(any(Blog.class))).thenReturn(updatedBlog);

            // Act
            blogService.modifyBlog(updateBlogRequest, true);

            // Assert
            verify(blogRepo).save(argThat(blog -> !blog.getBlogStatus().equals("Borrador")));
        }
    }
}

