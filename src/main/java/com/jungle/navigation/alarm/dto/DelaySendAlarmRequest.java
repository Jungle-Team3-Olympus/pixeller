package com.jungle.navigation.alarm.dto;

import java.sql.Timestamp;

public record DelaySendAlarmRequest(Long targetId, String alarmType, Timestamp targetTime)
		implements SendAlarmRequest {

	@Override
	public String type() {
		return alarmType;
	}

	@Override
	public Long targetId() {
		return targetId;
	}
}
