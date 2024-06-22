package com.widyu.healthcare.support.error;

import com.widyu.healthcare.support.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ErrorController {
  @ExceptionHandler(NoDataException.class)
  public ResponseEntity<Object> NoDataException(NoDataException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage(), 1000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(LoginSessionNullException.class)
  public ResponseEntity<Object> LoginSessionNullException(LoginSessionNullException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,2000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(MisMatchLoginException.class)
  public ResponseEntity<Object> MisMatchLoginException(MisMatchLoginException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,2001);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(DuplicateIdException.class)
  public ResponseEntity<Object> DuplicateIdException(DuplicateIdException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,2002);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(InsufficientPointsException.class)
  public ResponseEntity<Object> InsufficientPointsException(InsufficientPointsException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,3000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(MissingTokenException.class)
  public ResponseEntity<Object> MissingTokenException(MissingTokenException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,4000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MissingFileException.class)
  public ResponseEntity<Object> MissingFileException(MissingFileException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,5000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FailSmsSendException.class)
  public ResponseEntity<Object> FailSmsSendException(FailSmsSendException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,6000);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(MisMatchSmsCodeException.class)
  public ResponseEntity<Object> MisMatchSmsCodeException(MisMatchSmsCodeException e) {
    ErrorResponse response = new ErrorResponse(false, e.getMessage() ,6001);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

}
