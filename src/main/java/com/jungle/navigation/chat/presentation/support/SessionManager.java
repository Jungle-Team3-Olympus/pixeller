package com.jungle.navigation.chat.presentation.support;

import java.util.Map;
import java.util.Objects;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
	public <T> T getValue(SimpMessageHeaderAccessor accessor, String key, Class<T> type) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new IllegalArgumentException("SessionAttributes가 null입니다.");
		}

		return (T) sessionAttributes.get(key);
	}

	public void setValue(SimpMessageHeaderAccessor headerAccessor, String key, Object value) {
		headerAccessor.getSessionAttributes().put(key, value);
	}
}
