package com.jungle.navigation.product.service;

import com.jungle.navigation.product.dto.PriceDto;
import com.jungle.navigation.product.entity.AuctionEntity;
import com.jungle.navigation.product.entity.AuctionResultEntity;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.repository.AuctionRepository;
import com.jungle.navigation.product.repository.AuctionResultRepository;
import com.jungle.navigation.product.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final AuctionResultRepository auctionResultRepository;
	private final ProductsRepository productsRepository;

	public AuctionService(
			AuctionRepository auctionRepository,
			AuctionResultRepository auctionResultRepository,
			ProductsRepository productsRepository) {
		this.auctionRepository = auctionRepository;
		this.auctionResultRepository = auctionResultRepository;
		this.productsRepository = productsRepository;
	}

	// 버튼을 누르던 말을 하던 bidPrice를 입력 받으면
	@Transactional
	public AuctionEntity createAuction(int productId, int memberId, int bidPrice) {

		AuctionEntity newAuction = new AuctionEntity();

		newAuction.setProductId(productId);
		newAuction.setBidPrice(bidPrice);
		newAuction.setBidTime(Timestamp.valueOf(LocalDateTime.now()));
		newAuction.setMemberId(memberId);
		newAuction.setAuctionResultId(-1);

		return auctionRepository.save(newAuction);
	}

	// 경매 결과 저장, 해당 경매 로우 resultId 업데이트
	@Transactional
	public AuctionResultEntity closeAuction(int auctionId) {

		// 낙찰 받은 엔티티 가져오기
		AuctionEntity mostBid =
				auctionRepository
						.findById(auctionId)
						.orElseThrow(() -> new RuntimeException("Auction not found"));

		// 경매 상품 정보
		ProductEntity productInfo =
				productsRepository
						.findById(mostBid.getProductId())
						.orElseThrow(() -> new RuntimeException("Product not found"));

		AuctionResultEntity auctionResult = new AuctionResultEntity();
		auctionResult.setAuctionId(auctionId);
		auctionResult.setBidPrice(mostBid.getBidPrice());
		auctionResult.setMemberId(mostBid.getMemberId());
		auctionResult.setBidTime(Timestamp.valueOf(LocalDateTime.now()));
		auctionResult.setSellerId(productInfo.getMemberId());
		auctionResult.setProductId(productInfo.getProductId());

		auctionResult = auctionResultRepository.save(auctionResult);

		List<AuctionEntity> auctionList =
				auctionRepository.findAllByProductId(productInfo.getProductId());
		// 경매 엔티티에 auctionResultId 업데이트
		for (AuctionEntity auction : auctionList) {
			auction.setAuctionResultId(auctionResult.getAuctionResultId());
			System.out.println(auctionResult.getAuctionResultId());
		}
		auctionRepository.saveAll(auctionList);

		// 경매 결과 저장
		return auctionResult;
	}

	public AuctionResultEntity getAuctionResult(int auctionId) {

		return auctionResultRepository.findByAuctionId(auctionId);
	}

	public PriceDto getPrice(int productId) {

		ProductEntity presentProduct =
				productsRepository
						.findById(productId)
						.orElseThrow(() -> new RuntimeException("Product not found"));
		int primaryPrice = presentProduct.getPrice();

		List<AuctionEntity> bidList = auctionRepository.findAllByProductId(productId);
		Optional<AuctionEntity> mostBidEntity =
				bidList.stream().max(Comparator.comparing(AuctionEntity::getBidPrice));

		// PriceDto 객체 생성
		PriceDto priceDto = new PriceDto();

		// 가장 높은 입찰 가격이 없을 경우 기본 가격 설정
		// 가장 높은 입찰 가격이 있을 경우 해당 가격 설정
		if (mostBidEntity.isPresent()) {
			priceDto.setPresentPrice(mostBidEntity.get().getBidPrice());
		} else {
			priceDto.setPresentPrice(primaryPrice);
		}

		return priceDto;
	}
}
