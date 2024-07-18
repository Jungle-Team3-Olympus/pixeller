package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "product")
@ToString
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int productId;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "product_name")
	private String name;

	private String category;

	private int price;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "reg_date")
	private Timestamp createdAt;

	@Column(name = "sale_yn")
	private char saleYn;

	@Column(name = "use_yn")
	private char useYn;

	@Column(name = "auction_start_time")
	private Timestamp auctionStartTime;

	public void setMemberId(Long memberId) {
		this.memberId = Math.toIntExact(memberId);
	}
}
