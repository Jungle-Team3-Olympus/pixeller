package com.jungle.navigation.alarm.event;

import com.jungle.navigation.alarm.domain.AlarmType;
import java.sql.Timestamp;

public record DelayAlarmEvent(
		String type, Long targetId, Long productId, String productName, Timestamp targetTime)
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

	@Override
	public String productName() {
		return productName;
	}

	public static DelayAlarmEvent of(
			Long targetId, Long productId, String productName, Timestamp targetTime) {
		return new DelayAlarmEvent(
				AlarmType.AUCTION_START.getType(), targetId, productId, productName, targetTime);
	}
}
