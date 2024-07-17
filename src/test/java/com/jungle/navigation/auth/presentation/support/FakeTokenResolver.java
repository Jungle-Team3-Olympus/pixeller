package com.jungle.navigation.auth.presentation.support;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
@Profile("test")
public class FakeTokenResolver implements TokenResolver {
	private static final Long INITIAL_MEMBER_ID = 1L;

	@Override
	public MemberInfo decode(String value) {
		Long currentMemberId = INITIAL_MEMBER_ID + 1;
		return new MemberInfo(currentMemberId, "memberName" + currentMemberId);
	}
}
