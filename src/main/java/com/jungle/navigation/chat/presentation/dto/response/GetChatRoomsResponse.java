package com.jungle.navigation.chat.presentation.dto.response;

public record GetChatRoomsResponse(
		Long chatRoomId, String name, String message, int notReadCount) {}
