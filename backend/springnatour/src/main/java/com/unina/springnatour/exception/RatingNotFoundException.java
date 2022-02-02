package com.unina.springnatour.exception;

public class RatingNotFoundException extends RuntimeException{

    public RatingNotFoundException(Long id) {
        super("Could not find rating" + id);
    }
}
