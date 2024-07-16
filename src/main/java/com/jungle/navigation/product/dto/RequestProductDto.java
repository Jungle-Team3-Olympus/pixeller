package com.jungle.navigation.product.dto;

import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

public record RequestProductDto(
		@NotBlank(message = "상품이름을 기입해주세요.") String name,
		@NotBlank(message = "카테고리를 기입해주세요.") String category,
		@Min(value = 1000, message = "상품의 최소 가격은 1000원 입니다.") @NotNull(message = "원하시는 최소 가격을 책정해주세요.")
				int price,
		@Size(min = 10, max = 50, message = "최소 10자 이상 최대 50자 이하입니다.")
				@NotBlank(message = "상품 설명을 기입해주세요.")
				String description,
		List<File> files,
		@Future(message = "올바른 시간을 입력해 주세요.") @NotNull(message = "원하는 경매 시작 시간을 입력해 주세요.")
				Timestamp auctionStartTime) {
	public record File(
			@NotBlank(message = "image file을 등록해주세요.") String filename,
			@NotBlank(message = "image file을 등록해주세요.") String path) {}
}
