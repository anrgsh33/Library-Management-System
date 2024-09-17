package com.example.lms.exceptionHandler;

import com.example.lms.exception.CustomServiceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseModel> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<ResponseModel> handleCustomServiceException(CustomServiceException ex) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

