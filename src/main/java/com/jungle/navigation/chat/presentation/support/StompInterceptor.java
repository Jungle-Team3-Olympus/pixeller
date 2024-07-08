package com.jungle.navigation.chat.presentation.support;

import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_ID;
import static com.jungle.navigation.chat.presentation.support.ChatConstants.MEMBER_NAME;

import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.auth.presentation.support.TokenResolver;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {
	private static final String AUTHORIZATION = "Authorization";
	private final TokenResolver tokenResolver;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();

		handleStompCommand(command, accessor);

		return ChannelInterceptor.super.preSend(message, channel);
	}

	private void handleStompCommand(StompCommand stompCommand, StompHeaderAccessor accessor) {
		switch (stompCommand) {
			case CONNECT:
				handleConnect(accessor);
				break;
			case SUBSCRIBE:
				log.info("websocket : subscribe");
				break;
			case UNSUBSCRIBE:
				log.info("websocket : unsubscribe");
				break;
			case SEND:
				log.info("websocket : send");
				break;
			case DISCONNECT:
				log.info("websocket : disconnect");
				break;
			case ERROR:
				log.debug("websocket : error");
				break;
		}
	}

	private void handleConnect(StompHeaderAccessor accessor) {
		String authorizationHeader = accessor.getFirstNativeHeader(AUTHORIZATION);

		MemberInfo memberInfo = tokenResolver.decode(authorizationHeader);

		setValue(accessor, MEMBER_ID, memberInfo.getMemberId());
		setValue(accessor, MEMBER_NAME, memberInfo.getMemberName());
	}

	private void setValue(StompHeaderAccessor accessor, String key, Object value) {
		Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
		sessionAttributes.put(key, value);
	}

	private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new IllegalArgumentException("SessionAttributes가 null입니다.");
		}
		return sessionAttributes;
	}
}
