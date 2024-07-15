package com.jungle.navigation.product.controller;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.repository.PurchaseWishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-wish")
@Transactional
public class PurchaseWishController {

	private final PurchaseWishRepository purchaseWishRepository;

	@Autowired
	public PurchaseWishController(PurchaseWishRepository purchaseWishRepository) {
		this.purchaseWishRepository = purchaseWishRepository;
	}

	// 구매 희망 등록
	@PostMapping
	public ResponseEntity<String> addPurchaseWish(
			@RequestParam("productId") int productId, @Member MemberInfo memberInfo) {
		PurchaseWishEntity purchaseWish = new PurchaseWishEntity();
		purchaseWish.setMemberId(Math.toIntExact(memberInfo.getMemberId()));
		purchaseWish.setProductId(productId);

		purchaseWishRepository.save(purchaseWish);

		return ResponseEntity.status(HttpStatus.CREATED).body("Purchase wish saved successfully");
	}
}
