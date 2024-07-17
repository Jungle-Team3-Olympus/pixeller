package com.jungle.navigation.alarm.dto;

public record ImmediateSendAlarmRequest(String type, Long targetId) implements SendAlarmRequest {
	@Override
	public String type() {
		return type;
	}

	@Override
	public Long targetId() {
		return targetId;
	}
}
