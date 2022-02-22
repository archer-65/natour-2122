package com.unina.springnatour.repository;

import com.unina.springnatour.model.Compilation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Finds compilations by author
     * @param userId identifier of the user
     * @return List of Compilation
     */
    List<Compilation> findByUser_id(Long userId);

    /**
     * Finds compilations by author
     * @param userId identifier of the user
     * @return List of Compilation
     */
    List<Compilation> findByUser_id(Long userId, Pageable pageDetails);
}
