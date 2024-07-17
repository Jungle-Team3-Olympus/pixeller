package com.jungle.navigation.alarm.dto;

import com.jungle.navigation.alarm.domain.NotificationType;

public interface SendAlarmRequest {

	NotificationType type();

	Long targetId();
}
