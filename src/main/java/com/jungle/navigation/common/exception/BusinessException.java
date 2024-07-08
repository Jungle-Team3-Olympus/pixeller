package com.jungle.navigation.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
	private final HttpStatus httpStatus;

	public BusinessException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
