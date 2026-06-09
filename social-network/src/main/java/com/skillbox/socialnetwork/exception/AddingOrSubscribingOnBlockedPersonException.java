package com.skillbox.socialnetwork.exception;

/**
 * Исключение подписки заблокированного человека.
 */
public class AddingOrSubscribingOnBlockedPersonException extends Exception {
    public AddingOrSubscribingOnBlockedPersonException(String message) {
        super(message);
    }
}
