package com.jungle.navigation.auth.presentation.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeaderTokenExtractorTest {

	public static final String AUTHORIZATION = "Authorization";
	@Mock HttpServletRequest request;
	HeaderTokenExtractor headerTokenExtractor;

	@BeforeEach
	void setUp() {
		headerTokenExtractor = new HeaderTokenExtractor();
	}

	@Test
	@DisplayName("인증 헤더가 존재하지 않으면 예외가 발생한다.")
	void not_found_authorization_exception() {
		// given
		when(request.getHeader(AUTHORIZATION)).thenReturn(null);

		// when & then
		assertThrows(IllegalArgumentException.class, () -> headerTokenExtractor.extract(request));
	}

	@Test
	@DisplayName("인증 헤더가 존재하면 해당 값을 반환한다.")
	void get_authorization_value() {
		// given
		String value = "authorization header's value";
		when(request.getHeader(AUTHORIZATION)).thenReturn(value);

		// when
		String result = headerTokenExtractor.extract(request);

		// when & then
		assertEquals(value, result);
	}
}
