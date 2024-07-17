package com.jungle.navigation.alarm;

import com.jungle.navigation.alarm.domain.AlarmType;
import com.jungle.navigation.alarm.dto.SendAlarmRequest;
import com.jungle.navigation.alarm.pub.AlarmPublisher;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AlarmDispatcher {
	private final Map<AlarmType, AlarmPublisher> publishers;

	public AlarmDispatcher(List<AlarmPublisher> publishers) {
		this.publishers =
				publishers.stream().collect(Collectors.toMap(AlarmPublisher::support, Function.identity()));
	}

	public void dispatchAlarm(SendAlarmRequest request) {
		AlarmPublisher publisher = getAlarmPublisher(request.type());
		publisher.sendAlarm(request);
	}

	private AlarmPublisher getAlarmPublisher(String type) {
		AlarmType alarmType = AlarmType.get(type);

		return publishers.get(alarmType);
	}
}
