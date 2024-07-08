package com.jungle.navigation.auth.presentation.support;

public interface TokenResolver {
	Long decode(String value);
}
