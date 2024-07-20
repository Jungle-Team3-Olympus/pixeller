package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
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

	private int price;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Builder.Default
	@Column(nullable = false, updatable = false, name = "reg_date")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder.Default
	@Column(name = "sale_yn")
	private char saleYn = 'n';

	@Builder.Default
	@Column(name = "use_yn")
	private char useYn = 'y';

	public void setMemberId(Long memberId) {
		this.memberId = Math.toIntExact(memberId);
	}
}
