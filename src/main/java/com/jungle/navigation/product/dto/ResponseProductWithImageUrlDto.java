package com.jungle.navigation.product.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseProductWithImageUrlDto {

	private int productId;
	private String name;
	private double price;
	private String description;
	private char saleYn;
	private char useYn;
	private List<String> imageFileUrls;
	private MemberDto memberDto;

	public record MemberDto(int memberId, String id) {}
}
