package com.jungle.navigation.auth.presentation.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeaderTokenResolverTest {

	private static final String SECRET_KEY = "thisistestsecretkeythisistestsecretkey";

	private HeaderTokenResolver tokenResolver;
	private SecretKey secretKey;

	@BeforeEach
	void setUp() {
		tokenResolver = new HeaderTokenResolver(SECRET_KEY);
		secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("값 형식이 맞지 않으면 예외가 발생한다.")
	void not_value_format() {
		// given
		String invalid_token = "not validtoken";

		// when
		assertThrows(IllegalArgumentException.class, () -> tokenResolver.decode(invalid_token));
	}
}
