package com.jungle.navigation.auth.presentation.support;

public interface TokenResolver {
	MemberInfo decode(String value);
}
