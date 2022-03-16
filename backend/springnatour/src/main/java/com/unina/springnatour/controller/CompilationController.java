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

    /**
     * Gets a compilation
     *
     * @param id the identifier of the compilation
     * @return CompilationDto
     */
    @GetMapping("/compilations/{id}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long id) {

        CompilationDto compilationDto = compilationService.getCompilationById(id);

        return new ResponseEntity<CompilationDto>(compilationDto, HttpStatus.OK);
    }

    /**
     * Gets all the compilations
     *
     * @return List of CompilationDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/compilations/search")
    public ResponseEntity<List<CompilationDto>> getAllCompilationsByUserId(@RequestParam(name = "user_id") Long userId) {

        List<CompilationDto> compilationDtoList = compilationService.getAllCompilationsByUserId(userId);

        if (!compilationDtoList.isEmpty()) {
            return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the compilations
     *
     * @return List of CompilationDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/compilations/search_page")
    public ResponseEntity<List<CompilationDto>> getAllCompilationsByUserId(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(value = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {

        List<CompilationDto> compilationDtoList = compilationService.getAllCompilationsByUserId(userId, pageNo, pageSize);

        if (!compilationDtoList.isEmpty()) {
            return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new compilation
     *
     * @param compilationDto the CompilationDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/compilations/add")
    public ResponseEntity<?> addCompilation(@RequestBody CompilationDto compilationDto) {

        compilationService.addCompilation(compilationDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing compilation
     *
     * @param id             the identifier of the compilation
     * @param compilationDto the CompilationDTO Object containing the updated compilation
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/compilations/{id}/update")
    public ResponseEntity<?> updateCompilation(@PathVariable Long id,
                                               @RequestBody CompilationDto compilationDto) {

        compilationService.updateCompilation(id, compilationDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/compilations/{id}/add")
    public ResponseEntity<?> addRouteToCompilation(@PathVariable(value = "id") Long compilationId,
                                                   @RequestParam(value = "route_id") Long routeId) {

        compilationService.addRouteToCompilation(compilationId, routeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("compilations/{id}/remove")
    public ResponseEntity<?> removeRouteFromCompilation(@PathVariable(value = "id") Long compilationId,
                                                        @RequestParam(value = "route_id") Long routeId) {

        compilationService.removeRouteFromCompilation(compilationId, routeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete an existing compilation
     *
     * @param id the identifier of the compilation
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/compilations/{id}/delete")
    public ResponseEntity<?> deleteCompilation(@PathVariable Long id) {

        compilationService.deleteCompilation(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
