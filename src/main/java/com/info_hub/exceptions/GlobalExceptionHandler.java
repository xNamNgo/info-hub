package com.info_hub.exceptions;

import com.info_hub.responses.ErrorResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExcept.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(NotFoundExcept ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Fobidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(TokenException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    // send mail exception
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> messagingExceptionException(MessagingException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResetTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(ResetTokenException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle image must be < 5MB
     * @return STT 413
     */
    @ExceptionHandler(ImgSizeException.class)
    public ResponseEntity<ErrorResponse> handleImgException(ImgSizeException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    /**
     * Handle type must be an "image"
     * @return STT 415
     */
    @ExceptionHandler(MediaTypeException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeException(MediaTypeException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
