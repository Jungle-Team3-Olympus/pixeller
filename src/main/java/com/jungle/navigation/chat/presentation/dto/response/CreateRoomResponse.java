package com.jungle.navigation.chat.presentation.dto.response;

public record CreateRoomResponse(Long roomId) {
	public static CreateRoomResponse of(Long roomId) {
		return new CreateRoomResponse(roomId);
	}
}
