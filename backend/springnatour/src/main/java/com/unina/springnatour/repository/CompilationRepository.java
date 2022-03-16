package com.unina.springnatour.repository;

import com.unina.springnatour.model.Compilation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Finds compilations by author
     *
     * @param userId identifier of the user
     * @return List of Compilation
     */
    List<Compilation> findByUser_id(Long userId);

    /**
     * Finds compilations by author
     *
     * @param userId identifier of the user
     * @return List of Compilation
     */
    List<Compilation> findByUser_id(Long userId, Pageable pageDetails);

    @Modifying
    @Query(
            value = "insert into route_compilation (route_id, compilation_id) values (:route, :compilation)",
            nativeQuery = true
    )
    void insertRouteIntoCompilation(@Param("route") Long routeId, @Param("compilation") Long compilationId);

    @Modifying
    @Query(
            value = "delete from route_compilation rc where rc.route_id=:route and rc.compilation_id=:compilation",
            nativeQuery = true
    )
    void deleteRouteFromCompilation(@Param("route") Long routeId, @Param("compilation") Long compilationId);
}
