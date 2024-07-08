package com.jungle.navigation.chat.presentation.dto.response;

public record MessageResponse(Long roomId, String senderName, String message) {

	public static MessageResponse of(Long roomId, String senderName, String message) {
		return new MessageResponse(roomId, senderName, message);
	}
}
