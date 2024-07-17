package com.jungle.navigation.alarm.pub;

import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.dto.SendAlarmRequest;

public interface AlarmPublisher<T extends SendAlarmRequest> {
	AlarmType support();

	void sendAlarm(T request);
}
