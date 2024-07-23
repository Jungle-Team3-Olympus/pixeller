package com.jungle.navigation.chat.application.publisher;

public interface MessagePublisher {
	void send(String url, Object message);
}
