package com.unina.springnatour.service;

import com.unina.springnatour.dto.compilation.CompilationDto;
import com.unina.springnatour.dto.compilation.CompilationMapper;
import com.unina.springnatour.exception.CompilationNotFoundException;
import com.unina.springnatour.repository.CompilationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompilationService {

    @Autowired
    private CompilationRepository compilationRepository;

    @Autowired
    private CompilationMapper compilationMapper;

    /**
     * Gets a compilation
     *
     * @param id the identifier of the compilation
     * @return CompilationDTO Object after mapping from Entity, or throws Exception
     */
    public CompilationDto getCompilationById(Long id) {
        return compilationMapper.toDto(compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id)));
    }

    /**
     * Gets all the compilations for a certain user
     *
     * @param userId the identifier of the user
     * @return a List of CompilationDTO Objects after mapping from Entity, or throws Exception
     */
    public List<CompilationDto> getAllCompilationsByUserId(Long userId) {
        return compilationMapper.toDto(compilationRepository.findByUser_id(userId)
                .stream()
                .toList());
    }

    /**
     * Gets all the compilations for a certain user
     *
     * @param userId the identifier of the user
     * @return a List of CompilationDTO Objects after mapping from Entity, or throws Exception
     */
    public List<CompilationDto> getAllCompilationsByUserId(Long userId, Integer pageNo, Integer pageSize) {
        return compilationMapper.toDto(compilationRepository.findByUser_id(userId, PageRequest.of(pageNo, pageSize))
                .stream()
                .toList());
    }

    /**
     * Adds a compilation
     *
     * @param compilationDto CompilationDTO Object with required fields, mapped to Entity and saved
     */
    public void addCompilation(CompilationDto compilationDto) {
        compilationRepository.save(compilationMapper.toEntity(compilationDto));
    }

    /**
     * Updates a compilation
     *
     * @param id             the identifier of the compilation
     * @param compilationDto CompilationDTO Object, mapped to Entity, or throws Exception
     */
    public void updateCompilation(Long id, CompilationDto compilationDto) {
        compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id));
    }

    @Transactional
    public void addRouteToCompilation(Long compilationId, Long routeId) {
        compilationRepository.insertRouteIntoCompilation(routeId, compilationId);
    }

    @Transactional
    public void removeRouteFromCompilation(Long compilationId, Long routeId) {
        compilationRepository.deleteRouteFromCompilation(routeId, compilationId);
    }

    /**
     * Deletes a compilation
     *
     * @param id the identifier of the compilation
     */
    public void deleteCompilation(Long id) {
        compilationRepository.deleteById(id);
    }
}
