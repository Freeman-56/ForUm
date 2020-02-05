package com.example.forum.repository;

import com.example.forum.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    Iterable<Comment> findCommentsByPost_Id(Long id);
}
