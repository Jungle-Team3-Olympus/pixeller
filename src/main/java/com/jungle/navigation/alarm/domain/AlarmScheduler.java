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
	private long currentTime;
	private long targetTime;

	public AlarmScheduler(Timestamp target) {
		this.currentTime = TimeHelper.currentTimeMillis();
		this.targetTime = TimeHelper.toTimeMillis(target);

		validateTime(currentTime, targetTime);
	}

	private void validateTime(long now, long targetTime) {
		if (now >= targetTime) {
			throw new IllegalArgumentException("[Alarm] Invalid time");
		}
	}

	public String getDiff() {
		long diffTime = targetTime - currentTime;
		return String.valueOf(diffTime);
	}
}
