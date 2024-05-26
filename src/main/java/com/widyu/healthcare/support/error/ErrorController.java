package com.widyu.healthcare.support.error;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.widyu.healthcare.support.error.exception.DuplicateIdException;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;
import com.widyu.healthcare.support.error.exception.MissingTokenException;
import com.widyu.healthcare.support.error.exception.NoDataException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ErrorController {
  @ExceptionHandler(NoDataException.class)
  public ResponseEntity<Object> noDataException(NoDataException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage(), 101);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage().toString();
    ErrorResponse response = new ErrorResponse(false, message, 102);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(DuplicateIdException.class)
  public ResponseEntity<Object> handleDuplicateIdException(DuplicateIdException e, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,103);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InsufficientPointsException.class)
  public ResponseEntity<Object> InsufficientPointsException(InsufficientPointsException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,104);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MissingTokenException.class)
  public ResponseEntity<Object> TokenNullException(MissingTokenException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,105);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

}
