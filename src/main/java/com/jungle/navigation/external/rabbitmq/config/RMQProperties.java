package com.jungle.navigation.external.rabbitmq.config;

public interface RMQProperties {
	String WAITING_EXCHANGE_NAME = "exchange.waiting";
	String WAITING_ROUTING_KEY = "key.waiting.queue";
	String WAITING_QUEUE_NAME = "waiting.queue";

	String ALARM_EXCHANGE_NAME = "exchange.alarm";
	String ALARM_ROUTING_KEY = "key.alarm.queue";
	String ALARM_QUEUE_NAME = "alarm.queue";

	String CHAT_EXCHANGE_NAME = "exchange.chat";
	String CHAT_ROUTING_KEY = "key.chat.queue";
	String CHAT_QUEUE_NAME = "chat.queue";

	String DEAD_LETTER_EXCHANGE_NAME = "exchange.dead";
	String DEAD_LETTER_ROUTING_KEY = "key.dead.queue";
	String DEAD_LETTER_QUEUE_NAME = "dead.queue";
}
