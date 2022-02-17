package com.unina.springnatour.repository;

import com.unina.springnatour.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds posts by author
     * @param userId identifier of the user
     * @return List of Post
     */
    List<Post> findByUser_id(Long userId);
}