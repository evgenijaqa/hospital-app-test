package com.jane.project.common.exceptions;

public class DuplicateContactException extends RuntimeException {
    public DuplicateContactException(String message) {
        super(message);
    }
}
