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
public class AlarmQueueConfig {
	private static final String X_DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";
	private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
	private static final String X_MESSAGE_TTL_KEY = "x-message-ttl";
	private static final Long X_MESSAGE_TTL = 1000 * 60 * 30L;

	@Bean
	DirectExchange alarmExchange() {
		return new DirectExchange(RMQProperties.ALARM_EXCHANGE_NAME);
	}

	@Bean
	Queue alarmQueue() {
		return QueueBuilder.durable(RMQProperties.ALARM_QUEUE_NAME)
				.withArgument(X_DEAD_LETTER_EXCHANGE_KEY, RMQProperties.DEAD_LETTER_EXCHANGE_NAME)
				.withArgument(X_DEAD_LETTER_ROUTING_KEY, RMQProperties.DEAD_LETTER_EXCHANGE_NAME + ".alarm")
				.withArgument(X_MESSAGE_TTL_KEY, X_MESSAGE_TTL)
				.build();
	}

	@Bean
	Binding alarmBinding() {
		return BindingBuilder.bind(alarmQueue())
				.to(alarmExchange())
				.with(RMQProperties.ALARM_ROUTING_KEY);
	}
}
