package com.unina.springnatour.service;

import com.unina.springnatour.dto.compilation.CompilationDto;
import com.unina.springnatour.dto.compilation.CompilationMapper;
import com.unina.springnatour.exception.CompilationNotFoundException;
import com.unina.springnatour.repository.CompilationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompilationService {

    @Autowired
    private CompilationRepository compilationRepository;

    @Autowired
    private CompilationMapper compilationMapper;

    public CompilationDto getCompilationById(Long id) {
        return compilationMapper.toDto(compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id)));
    }

    public List<CompilationDto> getAllCompilationsByUserId(Long userId) {
        return compilationMapper.toDto(compilationRepository.findByUser_id(userId)
                .stream()
                .toList());
    }

    public void addCompilation(CompilationDto compilationDto) {
        compilationRepository.save(compilationMapper.toEntity(compilationDto));
    }


    public void updateCompilation(Long id, CompilationDto compilationDto) {
        compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id));
    }


    public void deleteCompilation(Long id) {
        compilationRepository.deleteById(id);
    }
}
