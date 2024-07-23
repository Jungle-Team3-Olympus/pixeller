package com.jungle.navigation.config;

import static com.jungle.navigation.chat.support.WebSocketEndpoints.CONNECT;
import static com.jungle.navigation.chat.support.WebSocketEndpoints.PUBLISH;
import static com.jungle.navigation.chat.support.WebSocketEndpoints.SUBSCRIBE;

import com.jungle.navigation.chat.presentation.support.StompHandshakeInterceptor;
import com.jungle.navigation.chat.presentation.support.StompInterceptor;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final String[] allowOriginUrlPatterns;
	private final StompInterceptor stompInterceptor;

	public WebSocketConfig(
			@Value("${cors.allow-origin.urls}") final String allowOriginUrlPatterns,
			StompInterceptor stompInterceptor) {
		this.allowOriginUrlPatterns =
				Arrays.stream(allowOriginUrlPatterns.split(",")).map(String::trim).toArray(String[]::new);
		this.stompInterceptor = stompInterceptor;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes(PUBLISH, SUBSCRIBE);
		registry.enableSimpleBroker(SUBSCRIBE);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint(CONNECT)
				.setAllowedOrigins(allowOriginUrlPatterns)
				.setHandshakeHandler(handshakeHandler())
				.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompInterceptor);
	}

	@Bean
	public DefaultHandshakeHandler handshakeHandler() {
		return new StompHandshakeInterceptor();
	}
}
