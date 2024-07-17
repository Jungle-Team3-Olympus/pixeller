package com.jungle.navigation.alarm.event;

import com.jungle.navigation.alarm.domain.AlarmType;

public record WishPurchaseEvent(String type, Long targetId, Long productId, String productName)
		implements AlarmEvent {
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

	public static WishPurchaseEvent of(Long targetId, Long productId, String productName) {
		return new WishPurchaseEvent(
				AlarmType.PURCHASE_REQUEST.getType(), targetId, productId, productName);
	}
}
