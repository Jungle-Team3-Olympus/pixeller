package com.jungle.navigation.auth.presentation.support;

import com.jungle.navigation.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeaderTokenExtractor implements TokenExtractor {
	@Override
	public String extract(HttpServletRequest request) {
		String value = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (value == null) {
			throw new BusinessException("인증되지 않은 사용자입니다.");
		}
		return value;
	}
}
