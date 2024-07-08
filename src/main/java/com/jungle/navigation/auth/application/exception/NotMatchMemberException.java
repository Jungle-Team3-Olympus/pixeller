package com.jungle.navigation.auth.application.exception;

import com.jungle.navigation.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotMatchMemberException extends BusinessException {
	public NotMatchMemberException() {
		super(HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "해당하는 정보를 가진 회원이 없습니다.";
	}
}
