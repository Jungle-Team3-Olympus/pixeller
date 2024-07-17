package com.jungle.navigation.alarm.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum NotificationType {
	PURCHASE_REQUEST("purchase_request"),
	AUCTION_START("auction_start");

	private final String type;

	NotificationType(String type) {
		this.type = type;
	}

	public static NotificationType get(String type) {
		return Arrays.stream(NotificationType.values())
				.filter(notificationType -> notificationType.getType().equals(type))
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("[notification type] not found"));
	}
}
