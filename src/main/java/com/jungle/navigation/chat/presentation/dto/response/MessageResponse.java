package com.jungle.navigation.chat.presentation.dto.response;

public record MessageResponse(boolean isBulk, Long roomId, String senderName, String message) {

	public static MessageResponse of(Long roomId, String senderName, String message) {
		return new MessageResponse(false, roomId, senderName, message);
	}
}
