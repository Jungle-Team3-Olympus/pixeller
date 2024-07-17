package com.jungle.navigation.alarm.pub;

import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.event.AlarmEvent;

public interface AlarmPublisher<T extends AlarmEvent> {
	AlarmType support();

	void sendAlarm(T request);
}
