package com.info_hub.exceptions;

import com.info_hub.dtos.responses.ErrorResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Throw when enum not match with server.
     * Like client request "APPROVE" but the enum of server is "APPROVED"
     * @return 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleEnumException(HttpMessageNotReadableException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeException(HttpClientErrorException.Unauthorized ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * handle an exception that Spring throws when validation fails: MethodArgumentNotValidException.
     * This exception is thrown during the @Valid or @Validated annotated parameter validation.
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
