package com.jungle.navigation.common.presentation.respnose;

import com.jungle.navigation.chat.application.publisher.AbstractMessage;
import java.util.List;

public record SliceResponse<T>(
		boolean isBulk, List<T> content, int currentPage, int size, boolean first, boolean last)
		implements AbstractMessage {
	public static <T> SliceResponse<T> of(
			List<T> content, int page, int size, boolean first, boolean last) {
		return new SliceResponse<>(true, content, page, size, first, last);
	}
}
