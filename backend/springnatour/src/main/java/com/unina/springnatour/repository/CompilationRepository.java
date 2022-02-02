package com.unina.springnatour.repository;

import com.unina.springnatour.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    public List<Compilation> findByUser_id(Long userId);
}
