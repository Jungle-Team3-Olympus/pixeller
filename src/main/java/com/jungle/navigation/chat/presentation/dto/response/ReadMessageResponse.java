package com.jungle.navigation.chat.presentation.dto.response;

import com.jungle.navigation.chat.application.publisher.AbstractMessage;

public record ReadMessageResponse(Long chatRoomId, Long messageId, int readCount)
		implements AbstractMessage {}
