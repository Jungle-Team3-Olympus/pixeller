package com.jungle.navigation.auth.presentation.support;

import com.jungle.navigation.auth.application.exception.NotAuthorizeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeaderTokenExtractor implements TokenExtractor {
	@Override
	public String extract(HttpServletRequest request) {
		String value = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (value == null) {
			throw new NotAuthorizeException();
		}
		return value;
	}
}
