package com.jungle.navigation.alarm.dto;

import com.jungle.navigation.alarm.domain.NotificationType;
import java.sql.Timestamp;

public record DelaySendAlarmRequest(
		Long targetId, NotificationType notificationType, Timestamp targetTime)
		implements SendAlarmRequest {

	@Override
	public NotificationType type() {
		return notificationType;
	}

	@Override
	public Long targetId() {
		return targetId;
	}
}
