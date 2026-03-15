package com.myblog.dao;

import com.myblog.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = CommentDao.class
        )
)
@ActiveProfiles("test")
class CommentDaoIntegrationTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long createPost() {
        jdbcTemplate.update(
                "INSERT INTO posts (title, text, likes_count) VALUES (?, ?, 0)",
                "t", "c"
        );
        return jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM posts",
                Long.class
        );
    }

    @Test
    void testUpdateChangesUpdatedAt() {
        Long postId = createPost();

        jdbcTemplate.update(
                "INSERT INTO comments (text, post_id) VALUES (?, ?)",
                "old",
                postId
        );

        Long commentId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM comments",
                Long.class
        );

        Comment before = commentDao.findById(commentId).orElseThrow();
        var beforeUpdatedAt = before.getUpdatedAt();

        before.setText("new");
        Comment after = commentDao.update(before);

        assertEquals("new", after.getText());
        assertFalse(after.getUpdatedAt().isBefore(beforeUpdatedAt));
    }

    @Test
    void testDeleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class, () -> commentDao.delete(999999L));
    }
}