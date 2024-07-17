package com.jungle.navigation.alarm.pub;

import com.jungle.navigation.alarm.dto.DelaySendAlarmRequest;

public interface AlarmPublisher {
	void sendAlarm(DelaySendAlarmRequest request);
}
