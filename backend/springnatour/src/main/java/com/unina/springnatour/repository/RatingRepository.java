package com.unina.springnatour.repository;

import com.unina.springnatour.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository <Rating, Long> {

}
