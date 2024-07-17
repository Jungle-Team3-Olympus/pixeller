package com.jungle.navigation.chat.presentation.support;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class StompHandshakeInterceptor extends DefaultHandshakeHandler {
	@Override
	protected Principal determineUser(
			ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		return new StompPrincipal(UUID.randomUUID().toString());
	}
}
