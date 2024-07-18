package com.jungle.navigation.alarm.pub;

import static com.jungle.navigation.alarm.config.RMQProperties.WAITING_EXCHANGE_NAME;
import static com.jungle.navigation.alarm.config.RMQProperties.WAITING_ROUTING_KEY;

import com.jungle.navigation.alarm.Alarm;
import com.jungle.navigation.alarm.domain.AlarmScheduler;
import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.event.DelayAlarmEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionStartAlarmPublisher implements AlarmPublisher<DelayAlarmEvent> {
	private final RabbitTemplate rabbitTemplate;

	@Override
	public AlarmType support() {
		return AlarmType.AUCTION_START;
	}

	@Override
	public void sendAlarm(@RequestBody DelayAlarmEvent event) {
		Alarm alarm =
				new Alarm(
						event.targetId(),
						AlarmType.AUCTION_START.name(),
						event.productId(),
						event.productName());
		AlarmScheduler alarmScheduler = new AlarmScheduler(event.targetTime());

		this.rabbitTemplate.convertAndSend(
				WAITING_EXCHANGE_NAME,
				WAITING_ROUTING_KEY,
				alarm,
				message -> {
					message
							.getMessageProperties()
							.setHeader(
									AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Alarm.class.getName());
					message.getMessageProperties().setExpiration(alarmScheduler.getTimeToLive());
					return message;
				});
		log.info("[지연 알람 예약] - [{}]", LocalDateTime.now());
	}
}
