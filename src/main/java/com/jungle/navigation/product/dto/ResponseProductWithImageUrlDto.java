package com.jungle.navigation.product.dto;

import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseProductWithImageUrlDto {

	private int productId;
	private String name;
	private String category;
	private double price;
	private String description;
	private char saleYn;
	private char useYn;
	private List<String> imageFileUrls;
	private MemberDto memberDto;
	private Timestamp auctionStartTime;

	public static record MemberDto(
			int id,
			char userType,
			float x,
			float y,
			Timestamp lastLogin,
			Timestamp joinedAt,
			String username,
			String email,
			String direction,
			String googleIdentity) {}
}
