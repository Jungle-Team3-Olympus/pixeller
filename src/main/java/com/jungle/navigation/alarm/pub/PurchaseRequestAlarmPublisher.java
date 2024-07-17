package com.jungle.navigation.alarm.pub;

import static com.jungle.navigation.alarm.pub.AlarmConstant.ALARM_DESTINATION;

import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.dto.DelaySendAlarmRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestAlarmPublisher implements AlarmPublisher<DelaySendAlarmRequest> {
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public AlarmType support() {
		return AlarmType.PURCHASE_REQUEST;
	}

	@Override
	public void sendAlarm(DelaySendAlarmRequest request) {
		messagingTemplate.convertAndSendToUser(
				request.targetId().toString(), ALARM_DESTINATION, request.type());
		log.info("[즉시 알람 전송] - [{}]", LocalDateTime.now());
	}
}
