package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "purchase_wish")
public class PurchaseWishEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "product_id")
	private int productId;
}
