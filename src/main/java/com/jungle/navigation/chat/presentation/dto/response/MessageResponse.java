package com.jungle.navigation.chat.presentation.dto.response;

import com.jungle.navigation.chat.application.publisher.AbstractMessage;

public record MessageResponse(boolean isBulk, Long roomId, String senderName, String message)
		implements AbstractMessage {

	public static MessageResponse of(Long roomId, String senderName, String message) {
		return new MessageResponse(false, roomId, senderName, message);
	}
}
