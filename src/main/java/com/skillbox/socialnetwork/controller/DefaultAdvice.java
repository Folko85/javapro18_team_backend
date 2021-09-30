package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.exceptionDTO.BadRequestResponse;
import com.skillbox.socialnetwork.exception.PostLikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<BadRequestResponse> handleRegisterUserExistException(UserExistException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Пользователь уже существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handlePostNotFoundException(PostNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Пост не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleUsernameNotFoundException(UsernameNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Пользователь не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PostLikeNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleUsernameNotFoundException(PostLikeNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Like не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
