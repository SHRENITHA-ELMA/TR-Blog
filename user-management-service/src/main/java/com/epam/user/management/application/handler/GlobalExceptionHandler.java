package com.epam.user.management.application.handler;
import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.exception.InvalidTokenException;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponse response=ApiResponse.builder()
                .message(ex.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidTokenException.class, UnauthorizedAccessException.class})
    public ResponseEntity<ApiResponse<Object>> handleInvalidTokenException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @Override
//    public ResponseEntity<ApiResponse<Map<String,String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//
//        ApiResponse<Map<String,String>> apiResponse = ApiResponse.<Map<String,String>>builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .message("Validation failed")
//                .data(errors) // Include validation errors in the response
//                .build();
//
//        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//    }
}
