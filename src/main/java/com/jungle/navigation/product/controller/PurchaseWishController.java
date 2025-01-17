package com.jungle.navigation.product.controller;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.product.dto.ResponsePurchaseWishListDto;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.service.PurchaseWishService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-wish")
@Transactional
public class PurchaseWishController {

	private final PurchaseWishService purchaseWishService;

	public PurchaseWishController(PurchaseWishService purchaseWishService) {
		this.purchaseWishService = purchaseWishService;
	}

	// 구매 희망 등록
	@PostMapping
	public ResponseEntity<PurchaseWishEntity> addPurchaseWish(
			@RequestParam("productId") int productId, @Member MemberInfo memberInfo) {
		PurchaseWishEntity purchaseWishEntity =
				purchaseWishService.savePurchaseWish(productId, Math.toIntExact(memberInfo.getMemberId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(purchaseWishEntity);
	}

	// 구매 희망자 리스트 반환
	@GetMapping
	public ResponseEntity<List<ResponsePurchaseWishListDto>> getPurchaseWishList(
			@RequestParam("productId") int productId) {

		List<ResponsePurchaseWishListDto> responsePurchaseWishListDto =
				purchaseWishService.findPurchaseWishMember(productId);

		return ResponseEntity.ok(responsePurchaseWishListDto);
	}
}
