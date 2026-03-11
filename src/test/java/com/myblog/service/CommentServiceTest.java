package com.myblog.service;

import com.myblog.dao.CommentDao;
import com.myblog.dto.CreateCommentRequest;
import com.myblog.dto.UpdateCommentRequest;
import com.myblog.model.Comment;
import com.myblog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentDao commentDao;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("Test comment");
        testComment.setPostId(1L);
    }

    @Test
    void testGetCommentsByPostId() {
        List<Comment> comments = Arrays.asList(testComment);
        when(commentDao.findByPostId(1L)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByPostId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testComment.getId(), result.get(0).getId());

        verify(commentDao).findByPostId(1L);
    }

    @Test
    void testGetCommentById() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));

        Optional<Comment> result = commentService.getCommentById(1L);

        assertTrue(result.isPresent());
        assertEquals(testComment.getId(), result.get().getId());

        verify(commentDao).findById(1L);
    }

    @Test
    void testCreateComment() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setText("New comment");
        request.setPostId(1L);

        when(commentDao.create(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.createComment(request);

        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());

        verify(commentDao).create(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setId(1L);
        request.setText("Updated comment");
        request.setPostId(1L);

        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentDao.update(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.updateComment(1L, request);

        assertNotNull(result);

        verify(commentDao).findById(1L);
        verify(commentDao).update(any(Comment.class));
    }

    @Test
    void testUpdateCommentNotFound() {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setId(999L);
        request.setText("Updated comment");
        request.setPostId(1L);

        when(commentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(999L, request));

        verify(commentDao).findById(999L);
        verify(commentDao, never()).update(any(Comment.class));
    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment(1L);

        verify(commentDao).delete(1L);
    }
}