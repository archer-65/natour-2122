package com.unina.springnatour.repository;

import com.unina.springnatour.model.route.Route;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Finds routes by author
     *
     * @param userId identifier of the user
     * @return List of Routes
     */
    List<Route> findByUser_id(Long userId);

    /**
     * Finds routes by author (paginated)
     *
     * @param userId identifier of the user
     * @return List of Routes
     */
    List<Route> findByUser_id(Long userId, Pageable pageDetails);

    /**
     * Finds routes by filter
     *
     * @param filter the search criteria
     * @return List of Routes
     */
    List<Route> findAll(@Nullable Specification<Route> filter, Pageable pageDetails);

    List<Route> findByTitleContainingIgnoreCase(String title, Sort sort);

    @Query("select R from Route R join R.compilations C where C.id = :compilationId")
    List<Route> findByCompilation(Long compilationId, Pageable pageDetails);
}
