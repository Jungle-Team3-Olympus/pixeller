package com.jungle.navigation.alarm.event;

import com.jungle.navigation.alarm.domain.AlarmType;

public record WishPurchaseEvent(Long targetId, Long productId) implements AlarmEvent {
	@Override
	public String type() {
		return AlarmType.PURCHASE_REQUEST.getType();
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
