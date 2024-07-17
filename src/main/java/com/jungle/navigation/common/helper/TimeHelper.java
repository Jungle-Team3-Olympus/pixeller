package com.jungle.navigation.common.helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeHelper {
	private static final String SEOUL_ZONE_NAME = "Asia/Seoul";

	public static long currentTimeMillis() {
		return ZonedDateTime.now(ZoneId.of(SEOUL_ZONE_NAME)).toInstant().toEpochMilli();
	}

	public static long toTimeMillis(Timestamp timestamp) {
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(SEOUL_ZONE_NAME));
		return zonedDateTime.toInstant().toEpochMilli();
	}
}
