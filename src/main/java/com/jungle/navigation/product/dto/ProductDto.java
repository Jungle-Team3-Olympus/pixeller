package com.jungle.navigation.product.dto;

import jakarta.validation.constraints.Min;

public record ProductDto(
		String name, String category, @Min(0) int price, String description, String username) {}
