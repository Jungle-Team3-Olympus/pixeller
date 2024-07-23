package com.jungle.navigation.auth.presentation.support;

import static com.jungle.navigation.auth.presentation.support.AuthConstants.PREFIX;

import com.jungle.navigation.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class HeaderTokenResolver implements TokenResolver {
	private static final int TOKEN_INDEX = 1;
	private static final int INVALID_VALUE_SIZE = 2;
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

	private Claims parseClaims(String token, SecretKey secretKey) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (MalformedJwtException e) {
			throw new BusinessException("Invalid JWT Token" + e.getMessage());
		} catch (ExpiredJwtException e) {
			throw new BusinessException("Expired JWT Token" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new BusinessException("Unsupported JWT Token" + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new BusinessException("JWT claims string is empty." + e.getMessage());
		}
	}

	private SecretKey generateSecretKey(String key) {
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

	private <T> T getClaimValue(Claims claims, String claimKey, Class<T> clazz) {
		return claims.get(claimKey, clazz);
	}

	private String parsePrefix(String token) {
		List<String> tokens = Arrays.stream(token.split(PREFIX)).map(String::trim).toList();

		if (tokens.size() != INVALID_VALUE_SIZE) {
			throw new IllegalArgumentException("잘못된 형식의 토큰입니다.");
		}
		return tokens.get(TOKEN_INDEX);
	}

	private MemberInfo getMemberInfo(Claims claims) {
		Long memberId = getClaimValue(claims, MEMBER_ID, Long.class);
		String memberName = getClaimValue(claims, MEMBER_NAME, String.class);

		return MemberInfo.builder().memberId(memberId).memberName(memberName).build();
	}
}
