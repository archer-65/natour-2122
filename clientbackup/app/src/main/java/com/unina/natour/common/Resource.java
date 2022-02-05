package com.unina.natour.common;


/**
 * Generic class that holds a result success or an error exception
 */
public abstract class Resource<T> {

    // Hide the private constructor to limit subclass types
    // NOTE: simulates sealed class
    private Resource() {
    }

    public final static class Success<T> extends Resource<T> {

        public T data;
        
        public Success(T data) {
            this.data = data;
        }
    }

    public final static class Error<T> extends Resource<T> {

        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }

    public final static class Loading<T> extends Resource<T> {

        public Loading() {
        }
    }
}
