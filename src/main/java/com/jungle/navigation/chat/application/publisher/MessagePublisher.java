package com.jungle.navigation.chat.application.publisher;

public interface MessagePublisher<T> {
	void send(String url, T message);
}
