package com.skillbox.socialnetwork.exception;

/**
 * Добавление подписки заблокированным пользователем.
 */
public class AddingOrSubscribingOnBlockerPersonException extends Exception {
    public AddingOrSubscribingOnBlockerPersonException(String message) {
        super(message);
    }
}
