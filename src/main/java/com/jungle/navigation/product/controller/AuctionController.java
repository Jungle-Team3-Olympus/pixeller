package com.jungle.navigation.product.controller;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.product.dto.PriceDto;
import com.jungle.navigation.product.entity.AuctionEntity;
import com.jungle.navigation.product.entity.AuctionResultEntity;
import com.jungle.navigation.product.service.AuctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

	private final AuctionService auctionService;

	public AuctionController(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	// 경매 중 입찰가격 DB에 쌓기
	@PostMapping("/{productId}")
	public ResponseEntity<AuctionEntity> createAuction(
			@PathVariable("productId") int productId,
			@Member MemberInfo memberInfo,
			@RequestParam("bidPrice") int bidPrice) {
		AuctionEntity newAuction =
				auctionService.createAuction(
						productId, Math.toIntExact(memberInfo.getMemberId()), bidPrice);
		return ResponseEntity.status(HttpStatus.CREATED).body(newAuction);
	}

	// 경매 종료시 DB에 정보 저장
	@PostMapping("/close/{auctionId}")
	public ResponseEntity<AuctionResultEntity> closeAuction(
			@PathVariable("auctionId") int auctionId) {
		try {
			AuctionResultEntity result = auctionService.closeAuction(auctionId);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// 경매 종료시 경매 종료(낙찰 정보) 조회
	@GetMapping("/result/{auctionId}")
	public ResponseEntity<AuctionResultEntity> getAuctionResult(
			@PathVariable("auctionId") int auctionId) {
		try {
			AuctionResultEntity result = auctionService.getAuctionResult(auctionId);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 현재 가격 불러오기
	@GetMapping("/price/{productId}")
	public ResponseEntity<PriceDto> getPresentPrice(@PathVariable("productId") int productId) {

		PriceDto priceDto = auctionService.getPrice(productId);
		return ResponseEntity.ok(priceDto);
	}
}
