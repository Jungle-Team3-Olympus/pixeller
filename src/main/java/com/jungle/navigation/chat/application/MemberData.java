package com.jungle.navigation.chat.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
public class MemberData {
	private Long memberId;
	private String name;

	public static MemberData of(int memberId, String name) {
		return new MemberData(Long.valueOf(memberId), name);
	}
}
