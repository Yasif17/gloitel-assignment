package com.authentication.registration.advices;

import com.authentication.registration.exceptions.InvalidCredentialsException;
import com.authentication.registration.exceptions.UserAlreadyExistException;
import com.authentication.registration.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(),err.getDefaultMessage()));

        ApiError error = new ApiError("Validation failed", 400, LocalDateTime.now(), fieldErrors);
        return ResponseEntity.badRequest().body(error);

    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiError> handleUserExists(UserAlreadyExistException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(ex.getMessage(), 409, LocalDateTime.now(), null));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handlerInvalidCredential(InvalidCredentialsException ex){
//        System.out.println(ex.getStackTrace());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(ex.getMessage(),401,LocalDateTime.now(),null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handlerUserNotFound(UserNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(),404,LocalDateTime.now(),null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex){
        ex.printStackTrace();
        ApiError apiError = new ApiError("Something went wrong",500,LocalDateTime.now(),null);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
