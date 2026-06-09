package com.skillbox.socialnetwork.handler;

import com.skillbox.socialnetwork.api.response.BadRequestResponse;
import com.skillbox.socialnetwork.exception.AddingOrSubscribingOnBlockedPersonException;
import com.skillbox.socialnetwork.exception.AddingOrSubscribingOnBlockerPersonException;
import com.skillbox.socialnetwork.exception.AddingYourselfToFriends;
import com.skillbox.socialnetwork.exception.ApiConnectException;
import com.skillbox.socialnetwork.exception.BlockAlreadyExistsException;
import com.skillbox.socialnetwork.exception.BlockingDeletedAccountException;
import com.skillbox.socialnetwork.exception.CommentNotFoundException;
import com.skillbox.socialnetwork.exception.DeletedAccountException;
import com.skillbox.socialnetwork.exception.DeletedAccountLoginException;
import com.skillbox.socialnetwork.exception.FriendshipExistException;
import com.skillbox.socialnetwork.exception.FriendshipNotFoundException;
import com.skillbox.socialnetwork.exception.LikeNotFoundException;
import com.skillbox.socialnetwork.exception.PostCreationExecption;
import com.skillbox.socialnetwork.exception.PostNotFoundException;
import com.skillbox.socialnetwork.exception.UnBlockingDeletedAccountException;
import com.skillbox.socialnetwork.exception.UnBlockingException;
import com.skillbox.socialnetwork.exception.UserBlocksHimSelfException;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.exception.UserUnBlocksHimSelfException;
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

/**
 * Контроллер исключений.
 */
@Slf4j
@ControllerAdvice
public class DefaultAdvice {

    private static final String INVALID_REQUEST = "invalid_request";

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<BadRequestResponse> handleRegisterUserExistException(UserExistException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription("Пользователь уже существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handlePostNotFoundException(PostNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription("Пост не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleUsernameNotFoundException(UsernameNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("unauthorized");
        badRequestResponse.setErrorDescription("Пользователь не существует");
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badRequestResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BadRequestResponse> handleAccessDeniedException(BadCredentialsException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("access_denied");
        badRequestResponse.setErrorDescription("Доступ запрещён");
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badRequestResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleLikeNotFoundException(LikeNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription("Like не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleCommentNotFoundException(CommentNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription("Comment не существует");
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleEntityNotFoundException(EntityNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(PostCreationExecption.class)
    public ResponseEntity<BadRequestResponse> handlePostCreationException(PostCreationExecption exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Вы не можете создавать пост на чужой странице");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<BadRequestResponse> handleFileSizeException(FileSizeLimitExceededException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError(INVALID_REQUEST);
        badRequestResponse.setErrorDescription(exc.getMessage() + ". It's have size " + exc.getActualSize()
                + " but expected less than " + exc.getPermittedSize());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(BlockAlreadyExistsException.class)
    public ResponseEntity<BadRequestResponse> handleBlockAlreadyExistsException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Already Exist");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UnBlockingException.class)
    public ResponseEntity<BadRequestResponse> handleUnBlockingException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block not Exists");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UserBlocksHimSelfException.class)
    public ResponseEntity<BadRequestResponse> handleUserBlocksHimSelfException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Blocks Him Self Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UserUnBlocksHimSelfException.class)
    public ResponseEntity<BadRequestResponse> handleUserUnBlocksHimSelfException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Unblocks Him Self Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(BlockingDeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleBlockingDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User Blocks Deleted account");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(UnBlockingDeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleUnBlockingDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("User UnBlocks Deleted account");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(DeletedAccountLoginException.class)
    public ResponseEntity<BadRequestResponse> handleDeletedAccountLoginException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Deleted account login");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(AddingOrSubscribingOnBlockerPersonException.class)
    public ResponseEntity<BadRequestResponse> handleAddingOrSubcribingOnBlockerPersonException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(AddingOrSubscribingOnBlockedPersonException.class)
    public ResponseEntity<BadRequestResponse> handleAddingOrSubscribingOnBlockedPersonException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Block Exception");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(DeletedAccountException.class)
    public ResponseEntity<BadRequestResponse> handleDeletedAccountException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Страница удалена");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(AddingYourselfToFriends.class)
    public ResponseEntity<BadRequestResponse> handleAddingYourselfToFriendsException(Exception exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Нельзя добавить себя в друзья");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(FriendshipNotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleFriendshipNotFoundException(FriendshipNotFoundException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Friend not found");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(FriendshipExistException.class)
    public ResponseEntity<BadRequestResponse> handleFriendshipExistException(FriendshipExistException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("You already friends");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка АПИ.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(ApiConnectException.class)
    public ResponseEntity<BadRequestResponse> handleApiConnectException(ApiConnectException exc) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setError("Error from external API");
        badRequestResponse.setErrorDescription(exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
