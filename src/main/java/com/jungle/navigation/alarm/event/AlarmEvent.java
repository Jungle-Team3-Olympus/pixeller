package com.jungle.navigation.alarm.event;

public interface AlarmEvent {

	String type();

	Long targetId();

	Long productId();
}
