package com.jungle.navigation.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** javax.validation.Valid 또는 @Validated binding error가 발생할 경우 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<BindingResult> handleBindException(BindException e) {
		log.warn("handleBindException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBindingResult());
	}

	/** 주로 @RequestParam enum으로 binding 못했을 경우 발생 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<String> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		log.warn("handleMethodArgumentTypeMismatchException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	/** 지원하지 않은 HTTP method 호출 할 경우 발생 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.warn("handleHttpRequestMethodNotSupportedException", e);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
	}

	/** 비즈니스 로직 실행 중 오류 발생 */
	@ExceptionHandler(value = {BusinessException.class})
	protected ResponseEntity<String> handleConflict(BusinessException e) {
		log.warn("BusinessException", e);
		return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
	}

	/** 나머지 예외 발생 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> handleException(Exception e) {
		log.error("Exception", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	protected ResponseEntity<String> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		log.warn("MethodArgumentNotValidException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
	}
}
