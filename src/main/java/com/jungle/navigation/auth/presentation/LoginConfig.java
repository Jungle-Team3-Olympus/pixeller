package com.jungle.navigation.auth.presentation;

import com.jungle.navigation.auth.presentation.interceptor.AuthInterceptor;
import com.jungle.navigation.auth.presentation.support.MemberArgumentResolver;
import com.jungle.navigation.auth.presentation.support.TokenExtractor;
import com.jungle.navigation.auth.presentation.support.TokenResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
	private final MemberArgumentResolver memberArgumentResolver;
	private final TokenResolver tokenResolver;
	private final TokenExtractor tokenExtractor;

	public LoginConfig(
			MemberArgumentResolver memberArgumentResolver,
			TokenResolver tokenResolver,
			TokenExtractor tokenExtractor) {
		this.memberArgumentResolver = memberArgumentResolver;
		this.tokenResolver = tokenResolver;
		this.tokenExtractor = tokenExtractor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberAuthInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(memberArgumentResolver);
	}

	@Bean
	public AuthInterceptor memberAuthInterceptor() {
		return AuthInterceptor.builder()
				.tokenExtractor(tokenExtractor)
				.tokenResolver(tokenResolver)
				.build();
	}
}
