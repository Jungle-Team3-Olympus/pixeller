package com.jungle.navigation.alarm;

import static com.jungle.navigation.alarm.config.RMQProperties.ALARM_QUEUE_NAME;
import static com.jungle.navigation.alarm.pub.AlarmConstant.getAlarmDestination;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmConsumer {
	private final SimpMessagingTemplate messagingTemplate;

	@RabbitListener(queues = ALARM_QUEUE_NAME)
	public void listenerDelayQueue(@Payload Alarm alarm, Message message, Channel channel) {
		try {
			log.info("[listener message] - [소비시간] - [{}] - [{}]", LocalDateTime.now(), alarm.toString());
			messagingTemplate.convertAndSend(getAlarmDestination(alarm.getTargetId()), alarm);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			log.error("[listener message] : ERROR ", e);
			try {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			} catch (IOException ioException) {
				log.error("[listener message] : Failed to nack message", ioException);
			}
		}
	}
}
