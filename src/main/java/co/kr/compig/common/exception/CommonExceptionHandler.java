package co.kr.compig.common.exception;

import co.kr.compig.common.exception.dto.ErrorCode;
import co.kr.compig.common.exception.dto.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleGlobalException(final Exception e,
      final WebRequest er) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(ErrorResponse.of(e, er), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BizException.class)
  protected ResponseEntity<ErrorResponse> handleBrewException(final BizException e,
      final WebRequest er) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(ErrorResponse.of(e, er), e.getErrorCode().getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> customHandleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {
    ErrorCode invalidInputValue = ErrorCode.INVALID_INPUT_VALUE;
    ErrorResponse errorResponse = ErrorResponse.of(invalidInputValue, request);
    List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getGlobalErrors()
        .stream()
        .map(objectError -> new ErrorResponse.FieldError(objectError.getObjectName(),
            objectError.getDefaultMessage())).toList();
    errorResponse.addFieldError(fieldErrors);

    return new ResponseEntity<>(errorResponse, invalidInputValue.getHttpStatus());
  }

  @ExceptionHandler(TypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException ex,
      WebRequest request) {
    ErrorResponse errorResponse = ErrorResponse.of(ex, request);
    List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();

    if (ex.getValue() != null) {
      fieldErrors.add(new ErrorResponse.FieldError((String) ex.getValue(), "잘못된 형식의 값을 입력하였습니다."));
    } else {
      fieldErrors.add(new ErrorResponse.FieldError("", "잘못된 형식의 값을 입력하였습니다."));
    }
    errorResponse.addFieldError(fieldErrors);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
