package com.jungle.navigation.chat.presentation.support;

import com.jungle.navigation.common.presentation.respnose.MessageCode;

public enum RoomSuccessMessageCode implements MessageCode {
	CREATE_ROOM("채팅방 생성을 성공했습니다.", "R-001"),
	GET_ROOM("채팅방 조회를 성공했습니다.", "R-002");
	private final String message;
	private final String code;

	RoomSuccessMessageCode(String message, String code) {
		this.message = message;
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getCode() {
		return code;
	}
}
