package com.jungle.navigation.alarm.domain;

import com.jungle.navigation.common.helper.TimeHelper;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Slf4j
public class AlarmScheduler {
	private static final long ONE_MINUTE_BEFORE_ALARM = 60000;

	private long currentTime;
	private long targetTime;

	public AlarmScheduler(Timestamp target) {
		this.currentTime = TimeHelper.currentTimeMillis();
		this.targetTime = TimeHelper.toTimeMillis(target);

		validateTime(currentTime, targetTime);
	}

	private void validateTime(long now, long targetTime) {
		if (isOneMinuteBefore(now, targetTime)) {
			throw new IllegalArgumentException(
					"[Alarm] Invalid time: Less than one minute before target time");
		}
	}

	public String getTimeToLive() {
		long diffTime = (targetTime - currentTime) - ONE_MINUTE_BEFORE_ALARM;
		log.info("diffTime: " + diffTime);
		return String.valueOf(diffTime);
	}

	public boolean isOneMinuteBefore(long now, long targetTime) {
		log.info("target time" + targetTime);
		log.info("now" + now);
		return (targetTime - currentTime) <= ONE_MINUTE_BEFORE_ALARM;
	}
}
