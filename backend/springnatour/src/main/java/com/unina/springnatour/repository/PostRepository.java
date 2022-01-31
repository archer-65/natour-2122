package com.unina.springnatour.repository;

import com.unina.springnatour.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
