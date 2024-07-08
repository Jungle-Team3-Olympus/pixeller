package com.jungle.navigation.chat.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateDirectRoomRequest(
		@NotNull(message = "메시지 수신자 아이디가 입력되지 않았습니다.") Long receiverId) {}
