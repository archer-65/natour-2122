package com.unina.springnatour.service;

import com.unina.springnatour.dto.rating.RatingDto;
import com.unina.springnatour.dto.rating.RatingMapper;
import com.unina.springnatour.exception.RatingNotFoundException;
import com.unina.springnatour.exception.UserNotFoundException;
import com.unina.springnatour.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    public RatingDto getRatingById(Long id) {
        return ratingMapper.toDto(ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id)));
    }

    public List<RatingDto> getAllRatings() {
        return ratingMapper.toDto(ratingRepository.findAll()
                .stream()
                .toList());
    }

    public void addRating(RatingDto ratingDto) {
        ratingRepository.save(ratingMapper.toEntity(ratingDto));
    }

    public void updateRating(Long id, RatingDto ratingDto) {
        ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));

        ratingRepository.save(ratingMapper.toEntity(ratingDto));
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
