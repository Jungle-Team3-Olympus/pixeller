package com.jungle.navigation.product.dto;

import jakarta.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.List;

public record RequestProductDto(
		String name,
		String category,
		@Min(0) int price,
		String description,
		List<File> files,
		Timestamp auctionStartTime) {
	public record File(String filename, String path) {}
}
