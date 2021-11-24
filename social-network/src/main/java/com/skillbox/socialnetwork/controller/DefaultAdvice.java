package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.BadRequestResponse;
import com.skillbox.socialnetwork.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@Slf4j
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
        badRequestResponse.setError("unauthorized");
        badRequestResponse.setErrorDescription("Пользователь не существует");
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badRequestResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BadRequestResponse> handleAccessDeniedException(BadCredentialsException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("access_denied");
        badRequestResponse.setErrorDescription("Доступ запрещён");
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badRequestResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleLikeNotFoundException(LikeNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Like не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleCommentNotFoundException(CommentNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription("Comment не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleEntityNotFoundException(EntityNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostCreationExecption.class)
    public ResponseEntity<BadRequestResponse> handlePostCreationException(PostCreationExecption exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Вы не можете создавать пост на чужой странице");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<BadRequestResponse> handleFileSizeException(FileSizeLimitExceededException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("invalid_request");
        badRequestResponse.setErrorDescription(exc.getMessage() + ". It's have size " + exc.getActualSize() +
                " but expected less than " + exc.getPermittedSize());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockAlreadyExistsException.class)
    public ResponseEntity<BadRequestResponse> handleBlockAlreadyExistsException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Already Exist");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnBlockingException.class)
    public ResponseEntity<BadRequestResponse> handleUnBlockingException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block not Exists");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBlocksHimSelfException.class)
    public ResponseEntity<BadRequestResponse> handleUserBlocksHimSelfException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Blocks Him Self Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserUnBlocksHimSelfException.class)
    public ResponseEntity<BadRequestResponse> handleUserUnBlocksHimSelfException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Unblocks Him Self Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockingDeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleBlockingDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Blocks Deleted account");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnBlockingDeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleUnBlockingDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User UnBlocks Deleted account");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeletedAccountLoginException.class)
    public ResponseEntity<BadRequestResponse> handleDeletedAccountLoginException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Deleted account login");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddingOrSubscribingOnBlockerPersonException.class)
    public ResponseEntity<BadRequestResponse> handleAddingOrSubcribingOnBlockerPersonException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddingOrSubscribingOnBlockedPersonException.class)
    public ResponseEntity<BadRequestResponse> handleAddingOrSubscribingOnBlockedPersonException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Страница удалена");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddingYourselfToFriends.class)
    public ResponseEntity<BadRequestResponse> handleAddingYourselfToFriendsException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Нельзя добавить себя в друзья");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FriendshipNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleFriendshipNotFoundException(FriendshipNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Friend not found");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FriendshipExistException.class)
    public ResponseEntity<BadRequestResponse> handleFriendshipExistException(FriendshipExistException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("You already friends");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
