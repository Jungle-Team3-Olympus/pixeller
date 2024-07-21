package com.jungle.navigation.rabbitmq.config.queue;

import static com.jungle.navigation.rabbitmq.config.RMQProperties.CHAT_EXCHANGE_NAME;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.CHAT_QUEUE_NAME;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.CHAT_ROUTING_KEY;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.DEAD_LETTER_EXCHANGE_NAME;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatQueueConfig {
	private static final String X_DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";
	private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
	private static final String X_MESSAGE_TTL_KEY = "x-message-ttl";
	private static final Long X_MESSAGE_TTL = 1000 * 60 * 30L;

	@Bean
	DirectExchange chatExchange() {
		return new DirectExchange(CHAT_EXCHANGE_NAME);
	}

	@Bean
	Queue chatQueue() {
		return QueueBuilder.durable(CHAT_QUEUE_NAME)
				.withArgument(X_DEAD_LETTER_EXCHANGE_KEY, DEAD_LETTER_EXCHANGE_NAME)
				.withArgument(X_DEAD_LETTER_ROUTING_KEY, DEAD_LETTER_EXCHANGE_NAME + ".chat")
				.withArgument(X_MESSAGE_TTL_KEY, X_MESSAGE_TTL)
				.build();
	}

	@Bean
	Binding chatBinding() {
		return BindingBuilder.bind(chatQueue()).to(chatExchange()).with(CHAT_ROUTING_KEY);
	}
}
