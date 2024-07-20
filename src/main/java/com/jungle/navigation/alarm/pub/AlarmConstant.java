package com.jungle.navigation.alarm.pub;

public class AlarmConstant {
	private static String ALARM_DESTINATION = "/sub/alarm";

	public static String getAlarmDestination(Long memberId) {
		return ALARM_DESTINATION + "/" + memberId;
	}
}
