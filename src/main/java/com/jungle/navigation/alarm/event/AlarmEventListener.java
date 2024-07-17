package com.jungle.navigation.alarm.event;

import com.jungle.navigation.alarm.AlarmDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventListener {
	private final AlarmDispatcher alarmDispatcher;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handle(AlarmEvent event) {
		log.info("alarm event 발생 : productId = {}", event.productId());
		alarmDispatcher.dispatchAlarm(event);
	}
}
