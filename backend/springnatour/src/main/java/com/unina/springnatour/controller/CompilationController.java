package com.unina.springnatour.controller;

import com.unina.springnatour.dto.compilation.CompilationDto;
import com.unina.springnatour.dto.post.PostDto;
import com.unina.springnatour.service.CompilationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompilationController {

    @Autowired
    private CompilationService compilationService;

    @GetMapping("/compilations/{id}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long id) {

        CompilationDto compilationDto = compilationService.getCompilationById(id);

        return new ResponseEntity<CompilationDto>(compilationDto, HttpStatus.OK);
    }

    @GetMapping("/compilations/search")
    public ResponseEntity<List<CompilationDto>> getAllCompilationsByUserId(@RequestParam Long userId) {

        List<CompilationDto> compilationDtoList = compilationService.getAllCompilationsByUserId(userId);

        if (!compilationDtoList.isEmpty()) {
            return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/compilations/add")
    public ResponseEntity<?> addCompilation(@RequestBody CompilationDto compilationDto) {

        compilationService.addCompilation(compilationDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/compilations/{id}/update")
    public ResponseEntity<?> updateCompilation(@PathVariable Long id,
                                               @RequestBody CompilationDto compilationDto) {

        compilationService.updateCompilation(id, compilationDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/compilations/{id}/delete")
    public ResponseEntity<?> deleteCompilation(@PathVariable Long id) {

        compilationService.deleteCompilation(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
