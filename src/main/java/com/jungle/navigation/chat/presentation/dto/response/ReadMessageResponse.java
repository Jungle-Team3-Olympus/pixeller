package com.jungle.navigation.chat.presentation.dto.response;

public record ReadMessageResponse(Long chatRoomId, Long messageId, int readCount) {}
