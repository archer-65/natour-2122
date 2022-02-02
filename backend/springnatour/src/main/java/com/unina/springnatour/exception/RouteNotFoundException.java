package com.unina.springnatour.exception;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(Long id) {
        super("Could not find route " + id);
    }
}
