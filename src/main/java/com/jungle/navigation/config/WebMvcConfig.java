package com.jungle.navigation.config;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
	private static final long MAX_AGE_SECS = 3600;
	private List<String> allowOriginUrlPatterns;
	public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

	public WebMvcConfig(@Value("${cors.allow-origin.urls}") final String allowOriginUrlPatterns) {
		this.allowOriginUrlPatterns =
				Arrays.stream(allowOriginUrlPatterns.split(",")).map(String::trim).toList();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] patterns = allowOriginUrlPatterns.toArray(String[]::new);

		registry
				.addMapping("/**")
				.allowedOriginPatterns(patterns)
				.allowedMethods(ALLOWED_METHOD_NAMES.split(","))
				.exposedHeaders("Authorization")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}
}
