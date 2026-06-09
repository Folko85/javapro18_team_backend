package com.skillbox.socialnetwork.exception;

/**
 * Исключение при взаимодействии по API.
 */
public class ApiConnectException extends Exception {
    public ApiConnectException(String error) {
        super(error);
    }
}
