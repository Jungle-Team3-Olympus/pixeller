package com.jungle.navigation.auth.presentation.support;

import static com.jungle.navigation.auth.presentation.support.AuthConstants.PREFIX;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeaderTokenResolver implements TokenResolver {
	private static final int TOKEN_INDEX = 1;
	private static final String MEMBER_ID = "uid";
	private SecretKey secretKey;

	public HeaderTokenResolver(@Value("${jwt.secretKey}") String secretKey) {
		this.secretKey = generateSecretKey(secretKey);
	}

	@Override
	public Long decode(String value) {
		String token = parsePrefix(value);
		Claims claims = parseClaims(token, secretKey);

		return getClaimValue(claims, MEMBER_ID);
	}

	private Claims parseClaims(final String token, final SecretKey secretKey) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (SignatureException e) {
			throw new IllegalArgumentException("signature error");
		} catch (Exception e) {
			throw new IllegalArgumentException("토큰 파싱 중 에러 발생");
		}
	}

	private SecretKey generateSecretKey(String key) {
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

	private Long getClaimValue(Claims claims, String claimKey) {
		return claims.get(claimKey, Long.class);
	}

	private String parsePrefix(String token) {
		return Arrays.stream(token.split(PREFIX)).map(String::trim).toList().get(TOKEN_INDEX);
	}
}
