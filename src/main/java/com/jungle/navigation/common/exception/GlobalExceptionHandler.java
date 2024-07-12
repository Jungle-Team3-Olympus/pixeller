package com.jungle.navigation.common.exception;

import com.jungle.navigation.common.presentation.respnose.ApiResponseBody;
import com.jungle.navigation.common.presentation.respnose.ApiResponseBody.FailureBody;
import com.jungle.navigation.slack.SlackAlarmGenerator;
import jakarta.servlet.http.HttpServletRequest;
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
	private final SlackAlarmGenerator slackAlarmGenerator;

	public GlobalExceptionHandler(SlackAlarmGenerator slackAlarmGenerator) {
		this.slackAlarmGenerator = slackAlarmGenerator;
	}

	/** javax.validation.Valid 또는 @Validated binding error가 발생할 경우 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<BindingResult> handleBindException(BindException e) {
		log.warn("handleBindException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBindingResult());
	}

	/** 주로 @RequestParam enum으로 binding 못했을 경우 발생 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		log.warn("handleMethodArgumentTypeMismatchException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	/** 지원하지 않은 HTTP method 호출 할 경우 발생 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.warn("handleHttpRequestMethodNotSupportedException", e);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
	}

	/** 비즈니스 로직 실행 중 오류 발생 */
	@ExceptionHandler(value = {BusinessException.class})
	public ResponseEntity<String> handleBusinessException(BusinessException e) {
		log.warn("BusinessException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	/** 나머지 예외 발생 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<FailureBody> unhandledException(Exception e, HttpServletRequest request) {
		log.error(
				"UnhandledException: {} {} errMessage={}\n",
				request.getMethod(),
				request.getRequestURI(),
				e.getMessage());
		slackAlarmGenerator.sendAlertErrorLog(e, request);
		return ResponseEntity.internalServerError()
				.body(new ApiResponseBody.FailureBody(false, "server error", "서비스에 문제가 발생했습니다."));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		log.warn("MethodArgumentNotValidException", e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
	}
}
