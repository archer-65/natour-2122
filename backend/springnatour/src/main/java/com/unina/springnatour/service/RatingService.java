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

    /**
     * Gets a rating
     * @param id the identifier of the rating
     * @return ratingDTO Object after mapping from Entity, or throws Exception
     */
    public RatingDto getRatingById(Long id) {
        return ratingMapper.toDto(ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id)));
    }

    /**
     * Gets all the ratings
     * @return a List of RatingDTO Objects after mappingfrom Entity, or throws Exception
     */
    public List<RatingDto> getAllRatings() {
        return ratingMapper.toDto(ratingRepository.findAll()
                .stream()
                .toList());
    }

    /**
     *Adds a rating
     * @param ratingDto ReportDTO Object with required fields, mapped to Entity and saved
     */
    public void addRating(RatingDto ratingDto) {
        ratingRepository.save(ratingMapper.toEntity(ratingDto));
    }

    /**
     * Updates a rating
     * @param id the identifier of the rating
     * @param ratingDto RatingDTO Object, mapped to Entity, or throws Exception
     */
    public void updateRating(Long id, RatingDto ratingDto) {
        ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));

        ratingRepository.save(ratingMapper.toEntity(ratingDto));
    }

    /**
     * Deletes a rating
     * @param id the identifier of the rating
     */
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
