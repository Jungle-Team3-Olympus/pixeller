package com.jungle.navigation.chat.infrastructure;

import com.jungle.navigation.chat.application.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryMessagePublisher implements MessagePublisher {
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void send(String url, Object message) {
		messagingTemplate.convertAndSend(url, message);
	}
}
