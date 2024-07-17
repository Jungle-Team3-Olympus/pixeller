package com.jungle.navigation.common.presentation.respnose;

import java.util.List;

public record SliceResponse<T>(
		List<T> content, int currentPage, int size, boolean first, boolean last) {}
