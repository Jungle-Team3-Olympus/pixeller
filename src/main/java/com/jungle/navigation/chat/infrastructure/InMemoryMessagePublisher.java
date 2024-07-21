package com.jungle.navigation.chat.infrastructure;

import com.jungle.navigation.chat.application.publisher.Message;
import com.jungle.navigation.chat.application.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryMessagePublisher<T extends Message> implements MessagePublisher<T> {
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void send(String url, T message) {
		messagingTemplate.convertAndSend(url, message);
	}
}
