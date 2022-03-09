package com.unina.springnatour.repository;

import com.unina.springnatour.model.post.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds posts by author
     * @param userId identifier of the user
     * @return List of Post
     */
    List<Post> findByUser_id(Long userId);

    /**
     * Finds posts by author (paginated)
     * @param userId identifier of the user
     * @return List of Post
     */
    List<Post> findByUser_id(Long userId, Pageable page);

    //@Query("SELECT P from Post P where P.route.id = :routeId AND P.reported = FALSE")
    List<Post> findByRoute_idAndReportedFalse(Long routeId, Pageable page);
}