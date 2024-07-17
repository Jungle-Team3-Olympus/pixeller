package com.jungle.navigation.alarm.event;

import com.jungle.navigation.alarm.domain.AlarmType;
import java.sql.Timestamp;

public record DelayAlarmEvent(Long targetId, Long productId, Timestamp targetTime)
		implements AlarmEvent {

	@Override
	public String type() {
		return AlarmType.AUCTION_START.getType();
	}

	@Override
	public Long targetId() {
		return targetId;
	}

	@Override
	public Long productId() {
		return productId;
	}
}
