package com.jungle.navigation.product.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataResponseDto<T> {
	private T data;

	public static <T> DataResponseDto<T> from(T data) {
		DataResponseDto<T> dto = new DataResponseDto<>();
		dto.setData(data);
		return dto;
	}
}
