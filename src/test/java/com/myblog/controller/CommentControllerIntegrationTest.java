package com.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.config.DatabaseConfig;
import com.myblog.config.RootConfig;
import com.myblog.config.WebConfig;
import com.myblog.dto.CreateCommentRequest;
import com.myblog.dto.CreatePostRequest;
import com.myblog.dto.UpdateCommentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class, DatabaseConfig.class})
@WebAppConfiguration
@Transactional
class CommentControllerIntegrationTest {

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        jdbcTemplate.execute("DELETE FROM post_images");
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    private Long createPost() throws Exception {
        CreatePostRequest req = new CreatePostRequest();
        req.setTitle("Post");
        req.setText("Content");
        req.setTags(Arrays.asList());

        String resp = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(resp).get("id").asLong();
    }

    private Long createComment(Long postId) throws Exception {
        CreateCommentRequest req = new CreateCommentRequest();
        req.setText("Hello");
        req.setPostId(postId);

        String resp = mockMvc.perform(post("/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    void testUpdateComment() throws Exception {
        Long postId = createPost();
        Long commentId = createComment(postId);

        UpdateCommentRequest update = new UpdateCommentRequest();
        update.setId(commentId);
        update.setPostId(postId);
        update.setText("Updated text");

        mockMvc.perform(put("/posts/" + postId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.text").value("Updated text"));
    }

    @Test
    void testUpdateCommentNotFound() throws Exception {
        Long postId = createPost();

        UpdateCommentRequest update = new UpdateCommentRequest();
        update.setId(999999L);
        update.setPostId(postId);
        update.setText("Updated text");

        mockMvc.perform(put("/posts/" + postId + "/comments/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteComment() throws Exception {
        Long postId = createPost();
        Long commentId = createComment(postId);

        mockMvc.perform(delete("/posts/" + postId + "/comments/" + commentId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/" + postId + "/comments/" + commentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCommentNotFound() throws Exception {
        Long postId = createPost();

        mockMvc.perform(delete("/posts/" + postId + "/comments/999999"))
                .andExpect(status().isNotFound());
    }
}
