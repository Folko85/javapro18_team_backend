package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.exceptionDTO.BadRequestResponse;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler(RegisterUserExistException.class)
    public ResponseEntity<BadRequestResponse> handleRegisterUserExistException(RegisterUserExistException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Пользователь уже существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
