package com.jungle.navigation.external.rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RMQConnectionConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private int port;

	@Value("${spring.rabbitmq.stream.virtual-host}")
	private String virtualHost;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(converter());
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setChannelTransacted(true);
		rabbitTemplate.setReplyTimeout(60000);

		return rabbitTemplate;
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setVirtualHost(virtualHost);
		factory.setUsername(username);
		factory.setPassword(password);

		return factory;
	}

	@Bean
	public MessageConverter converter() {
		DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
		defaultClassMapper.setTrustedPackages("*");

		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);

		return jackson2JsonMessageConverter;
	}
}
