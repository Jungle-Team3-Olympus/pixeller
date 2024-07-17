package com.jungle.navigation.chat.presentation.dto.response;

public record RoomResponse(Long roomId) {
	public static RoomResponse of(Long roomId) {
		return new RoomResponse(roomId);
	}
}
