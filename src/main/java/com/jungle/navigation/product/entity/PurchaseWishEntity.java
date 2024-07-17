package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name = "purchase_wish")
@ToString
public class PurchaseWishEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "product_id")
	private int productId;
}
