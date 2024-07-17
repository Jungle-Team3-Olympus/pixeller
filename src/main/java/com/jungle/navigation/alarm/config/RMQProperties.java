package com.jungle.navigation.alarm.config;

public interface RMQProperties {
	String WAITING_EXCHANGE_NAME = "exchange.waiting";
	String WAITING_ROUTING_KEY = "key.waiting.queue";
	String WAITING_QUEUE_NAME = "waiting.queue";

	String ALARM_EXCHANGE_NAME = "exchange.alarm";
	String ALARM_ROUTING_KEY = "key.alarm.queue";
	String ALARM_QUEUE_NAME = "alarm.queue";

	String DEAD_LETTER_EXCHANGE_NAME = "exchange.dead";
	String DEAD_LETTER_ROUTING_KEY = "key.dead.queue";
	String DEAD_LETTER_QUEUE_NAME = "dead.queue";
}
