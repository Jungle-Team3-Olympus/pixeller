package com.jungle.navigation.auth.presentation.support;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {
	String extract(HttpServletRequest request);
}
