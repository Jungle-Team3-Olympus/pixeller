package com.jungle.navigation.rabbitmq.config.queue;

import static com.jungle.navigation.rabbitmq.config.RMQProperties.ALARM_EXCHANGE_NAME;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.ALARM_ROUTING_KEY;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.WAITING_EXCHANGE_NAME;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.WAITING_QUEUE_NAME;
import static com.jungle.navigation.rabbitmq.config.RMQProperties.WAITING_ROUTING_KEY;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WaitingQueueConfig {
	private static final String X_DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";
	private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

	@Bean
	DirectExchange waitingExchange() {
		return new DirectExchange(WAITING_EXCHANGE_NAME);
	}

	@Bean
	Queue waitingQueue() {
		return QueueBuilder.durable(WAITING_QUEUE_NAME)
				.withArgument(X_DEAD_LETTER_EXCHANGE_KEY, ALARM_EXCHANGE_NAME)
				.withArgument(X_DEAD_LETTER_ROUTING_KEY, ALARM_ROUTING_KEY)
				.build();
	}

	@Bean
	Binding waitingBinding() {
		return BindingBuilder.bind(waitingQueue()).to(waitingExchange()).with(WAITING_ROUTING_KEY);
	}
}
