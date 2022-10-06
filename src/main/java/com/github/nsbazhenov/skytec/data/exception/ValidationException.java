package com.github.nsbazhenov.skytec.data.exception;

/**
 * Validation exception.
 *
 * @author Bazhenov Nikita
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
