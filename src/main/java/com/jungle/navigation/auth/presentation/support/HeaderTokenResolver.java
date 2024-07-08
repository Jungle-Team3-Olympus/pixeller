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
	private static final String MEMBER_NAME = "username";
	private SecretKey secretKey;

	public HeaderTokenResolver(@Value("${jwt.secretKey}") String secretKey) {
		this.secretKey = generateSecretKey(secretKey);
	}

	@Override
	public MemberInfo decode(String value) {
		String token = parsePrefix(value);
		Claims claims = parseClaims(token, secretKey);

		return getMemberInfo(claims);
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

	private <T> T getClaimValue(Claims claims, String claimKey, Class<T> clazz) {
		return claims.get(claimKey, clazz);
	}

	private String parsePrefix(String token) {
		return Arrays.stream(token.split(PREFIX)).map(String::trim).toList().get(TOKEN_INDEX);
	}

	private MemberInfo getMemberInfo(Claims claims) {
		Long memberId = getClaimValue(claims, MEMBER_ID, Long.class);
		String memberName = getClaimValue(claims, MEMBER_NAME, String.class);

		return MemberInfo.builder().memberId(memberId).memberName(memberName).build();
	}
}
