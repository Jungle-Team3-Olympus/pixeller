package com.jungle.navigation.alarm;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Alarm implements Serializable {
	@Serial private static final long serialVersionUID = -2825260350855140563L;

	private Long targetId;
	private String alarmType;
}
