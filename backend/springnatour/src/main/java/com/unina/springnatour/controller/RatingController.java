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

    @GetMapping("/ratings/{id}")
    public ResponseEntity<RatingDto> getRatingById(@PathVariable Long id) {

        RatingDto ratingDto = ratingService.getRatingById(id);

        return new ResponseEntity<>(ratingDto, HttpStatus.OK);
    }

    @GetMapping("/ratings")
    public ResponseEntity<List<RatingDto>> getAllRatings() {

        List<RatingDto> ratingDtoList = ratingService.getAllRatings();

        if (!ratingDtoList.isEmpty()) {
            return new ResponseEntity<>(ratingDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/ratings/add")
    public ResponseEntity<?> addRating(@RequestBody RatingDto ratingDto){

        ratingService.addRating(ratingDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/ratings/{id}/update")
    public ResponseEntity<?> updateRating(@PathVariable Long id,
                                          @RequestBody RatingDto ratingDto) {
        ratingService.updateRating(id, ratingDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/ratings/{id}/delete")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {

        ratingService.deleteRating(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
