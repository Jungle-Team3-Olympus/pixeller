package com.jungle.navigation.auth.presentation.support;

import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class FakeTokenResolver implements TokenResolver {
	private static final int TOKEN_INDEX = 1;

	@Override
	public MemberInfo decode(String token) {
		String value = parsePrefix(token);
		return new MemberInfo(Long.valueOf(value), value);
	}

	// BEARER 1
	private String parsePrefix(String token) {
		return Arrays.stream(token.split(AuthConstants.PREFIX))
				.map(String::trim)
				.toList()
				.get(TOKEN_INDEX);
	}
}
