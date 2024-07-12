package com.jungle.navigation.chat.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(@NotNull(message = "메시지 내용이 입력되지 않았습니다.") String content) {}
