package com.skillbox.socialnetwork.exception;

/**
 * Исключение при удалении аккаунта.
 */
public class DeletedAccountException extends Exception {
    public DeletedAccountException(String message) {
        super(message);
    }
}
