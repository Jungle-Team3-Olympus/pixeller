package com.jungle.navigation.alarm.config.queue;

import static com.jungle.navigation.alarm.config.RMQProperties.DEAD_LETTER_EXCHANGE_NAME;
import static com.jungle.navigation.alarm.config.RMQProperties.DEAD_LETTER_QUEUE_NAME;
import static com.jungle.navigation.alarm.config.RMQProperties.DEAD_LETTER_ROUTING_KEY;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterQueueConfig {
	private static final String ALARM = ".alarm";

	@Bean
	Queue alarmDeadLetterQueue() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME + ALARM).build();
	}

	@Bean
	DirectExchange alarmDeadLetterExchange() {
		return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE_NAME + ALARM).build();
	}

	@Bean
	Binding alarmDeadLetterBinding() {
		return BindingBuilder.bind(alarmDeadLetterQueue())
				.to(alarmDeadLetterExchange())
				.with(DEAD_LETTER_ROUTING_KEY + ALARM);
	}
}
