package com.jungle.navigation.alarm.pub;

import static com.jungle.navigation.alarm.pub.AlarmConstant.getAlarmDestination;

import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.event.WishPurchaseEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestAlarmPublisher implements AlarmPublisher<WishPurchaseEvent> {
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public AlarmType support() {
		return AlarmType.PURCHASE_REQUEST;
	}

	@Override
	public void sendAlarm(WishPurchaseEvent event) {
		messagingTemplate.convertAndSend(getAlarmDestination(event.targetId()), event);
		log.info("[즉시 알람 전송] - [{}]", LocalDateTime.now());
	}
}
