package com.unina.springnatour.repository;

import com.unina.springnatour.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find posts by author
     * @param userId identifier of the user
     * @return List of Post
     */
    public List<Post> findByUser_id(Long userId);
}
