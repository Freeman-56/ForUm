package com.example.forum.repository;

import com.example.forum.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
    Iterable<Post> findPostsByAuthor_Id(Long id);
}

