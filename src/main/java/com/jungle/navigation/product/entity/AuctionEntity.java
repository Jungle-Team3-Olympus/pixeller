package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "auction")
public class AuctionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auction_id")
	private int auctionId;

	@Column(name = "product_id", nullable = false)
	private int productId;

	@Column(name = "member_id", nullable = false)
	private int memberId;

	@Column(name = "bid_price")
	private int bidPrice;

	@Column(name = "bid_time")
	private Timestamp bidTime;

	@Column(name = "auction_result_id")
	private int auctionResultId;
}
