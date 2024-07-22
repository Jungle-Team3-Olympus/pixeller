package com.jungle.navigation.external.rabbitmq.config.queue;

import com.jungle.navigation.external.rabbitmq.config.RMQProperties;
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
		return new DirectExchange(RMQProperties.WAITING_EXCHANGE_NAME);
	}

	@Bean
	Queue waitingQueue() {
		return QueueBuilder.durable(RMQProperties.WAITING_QUEUE_NAME)
				.withArgument(X_DEAD_LETTER_EXCHANGE_KEY, RMQProperties.ALARM_EXCHANGE_NAME)
				.withArgument(X_DEAD_LETTER_ROUTING_KEY, RMQProperties.ALARM_ROUTING_KEY)
				.build();
	}

	@Bean
	Binding waitingBinding() {
		return BindingBuilder.bind(waitingQueue())
				.to(waitingExchange())
				.with(RMQProperties.WAITING_ROUTING_KEY);
	}
}
