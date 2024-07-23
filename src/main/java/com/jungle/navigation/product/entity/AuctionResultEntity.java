package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "auction_result")
public class AuctionResultEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auction_result_id")
	private int auctionResultId;

	@Column(name = "auction_id")
	private int auctionId;

	@Column(name = "product_id")
	private int productId;

	@Column(name = "seller_id")
	private int sellerId;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "bid_price")
	private int bidPrice;

	@Column(name = "bid_time")
	private Timestamp bidTime;
}
