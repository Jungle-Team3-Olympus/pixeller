package com.jungle.navigation.alarm.config.queue;

import static com.jungle.navigation.alarm.config.RMQProperties.ALARM_EXCHANGE_NAME;
import static com.jungle.navigation.alarm.config.RMQProperties.ALARM_QUEUE_NAME;
import static com.jungle.navigation.alarm.config.RMQProperties.ALARM_ROUTING_KEY;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlarmQueueConfig {
	@Bean
	DirectExchange alarmExchange() {
		return new DirectExchange(ALARM_EXCHANGE_NAME);
	}

	@Bean
	Queue alarmQueue() {
		return QueueBuilder.durable(ALARM_QUEUE_NAME).build();
	}

	@Bean
	Binding alarmBinding() {
		return BindingBuilder.bind(alarmQueue()).to(alarmExchange()).with(ALARM_ROUTING_KEY);
	}
}
