package com.unina.springnatour.repository;

import com.unina.springnatour.model.route.Route;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Find routes by author
     * @param userId identifier of the user
     * @return List of Routes
     */
    public List<Route> findByUser_id(Long userId);

    public List<Route> findAll(@Nullable Specification<Route> filter);
}
