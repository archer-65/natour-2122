package com.unina.springnatour.controller;

import com.unina.springnatour.dto.rating.RatingDto;
import com.unina.springnatour.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Gets a rating
     * @param id the identifier of the rating
     * @return RatingDTO
     */
    @GetMapping("/ratings/{id}")
    public ResponseEntity<RatingDto> getRatingById(@PathVariable Long id) {

        RatingDto ratingDto = ratingService.getRatingById(id);

        return new ResponseEntity<>(ratingDto, HttpStatus.OK);
    }

    /**
     * Gets all the ratings
     * @return List of RatingDTO
     */
    @GetMapping("/ratings")
    public ResponseEntity<List<RatingDto>> getAllRatings() {

        List<RatingDto> ratingDtoList = ratingService.getAllRatings();

        if (!ratingDtoList.isEmpty()) {
            return new ResponseEntity<>(ratingDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new rating
     * @param ratingDto the RatingDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/ratings/add")
    public ResponseEntity<?> addRating(@RequestBody RatingDto ratingDto){

        ratingService.addRating(ratingDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing rating
     * @param id the identifier of the rating
     * @param ratingDto the RatingDTO Object containing the updated rating
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/ratings/{id}/update")
    public ResponseEntity<?> updateRating(@PathVariable Long id,
                                          @RequestBody RatingDto ratingDto) {
        ratingService.updateRating(id, ratingDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes an existing rating
     * @param id the identifier of the rating
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/ratings/{id}/delete")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {

        ratingService.deleteRating(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
