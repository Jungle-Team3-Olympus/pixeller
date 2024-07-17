package com.jungle.navigation.alarm.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AlarmType {
	PURCHASE_REQUEST("purchase_request"),
	AUCTION_START("auction_start");

	private final String type;

	AlarmType(String type) {
		this.type = type;
	}

	public static AlarmType get(String type) {
		return Arrays.stream(AlarmType.values())
				.filter(alarmType -> alarmType.getType().equals(type))
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("[notification type] not found"));
	}
}
