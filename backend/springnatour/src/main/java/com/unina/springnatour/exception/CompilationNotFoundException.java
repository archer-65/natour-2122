package com.unina.springnatour.exception;

public class CompilationNotFoundException extends RuntimeException {

    public CompilationNotFoundException(Long id) {
        super("Could not find compilation " + id);
    }
}
