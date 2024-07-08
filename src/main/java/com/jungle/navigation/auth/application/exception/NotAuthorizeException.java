package com.jungle.navigation.auth.application.exception;

import com.jungle.navigation.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAuthorizeException extends BusinessException {
	public NotAuthorizeException() {
		super(HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "권한이 없는 사용자입니다";
	}
}
