package com.jungle.navigation.chat.presentation.dto.response;

import java.util.List;

public record MessagesResponse<T>(
		Long roomId, List<T> messages, boolean hasNext, int pageNumber, int pageSize) {}
