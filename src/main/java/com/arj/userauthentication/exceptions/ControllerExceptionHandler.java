package com.arj.userauthentication.exceptions;

import com.arj.userauthentication.dtos.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String ERROR_UNABLE_COMPLETE_REQUEST = "Unable to complete request.";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> details = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());

    ErrorResponse error = new ErrorResponse("Data validation failed", details);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    List<String> list = new ArrayList<>();
    list.add(ex.getMessage());

    ErrorResponse error = new ErrorResponse(ERROR_UNABLE_COMPLETE_REQUEST, list);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException ex) {
    List<String> list = new ArrayList<>();
    list.add(ex.getMessage());

    ErrorResponse error = new ErrorResponse(ERROR_UNABLE_COMPLETE_REQUEST, list);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    List<String> list = new ArrayList<>();
    list.add(ex.getMessage());

    ErrorResponse error = new ErrorResponse("Operation not allowed for this user level", list);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
    List<String> list = new ArrayList<>();

    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      list.add(violation.getMessage());
    }

    ErrorResponse error = new ErrorResponse(ERROR_UNABLE_COMPLETE_REQUEST, list);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

}
