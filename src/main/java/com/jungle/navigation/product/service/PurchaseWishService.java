package com.jungle.navigation.product.service;

import com.jungle.navigation.alarm.event.DelayAlarmEvent;
import com.jungle.navigation.alarm.event.WishPurchaseEvent;
import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.member.MemberJpaRepository;
import com.jungle.navigation.product.dto.ResponsePurchaseWishListDto;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.repository.ProductsRepository;
import com.jungle.navigation.product.repository.PurchaseWishRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PurchaseWishService {

	private final PurchaseWishRepository purchaseWishRepository;
	private final ProductsRepository productsRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final MemberJpaRepository memberJpaRepository;
	public PurchaseWishService(
			PurchaseWishRepository purchaseWishRepository,
			ProductsRepository productsRepository,
			ApplicationEventPublisher eventPublisher) {
		this.purchaseWishRepository = purchaseWishRepository;
		this.productsRepository = productsRepository;
		this.eventPublisher = eventPublisher;
		this.memberJpaRepository = memberJpaRepository;
	}

	@Transactional
	public PurchaseWishEntity savePurchaseWish(int productId, int memberId) {

		ProductEntity productEntity =
				productsRepository
						.findById(productId)
						.orElseThrow(() -> new BusinessException("Invalid productId " + productId));

		PurchaseWishEntity purchaseWish = new PurchaseWishEntity();
		purchaseWish.setMemberId(Math.toIntExact(memberId));
		purchaseWish.setProductId(productId);

		eventPublisher.publishEvent(
				DelayAlarmEvent.of(
						Long.valueOf(memberId),
						Long.valueOf(productId),
						productEntity.getName(),
						productEntity.getAuctionStartTime()));
		eventPublisher.publishEvent(
				WishPurchaseEvent.of(
						Long.valueOf(productEntity.getMemberId()),
						Long.valueOf(Long.valueOf(productId)),
						productEntity.getName()));

		return purchaseWishRepository.save(purchaseWish);
	}

	public List<ResponsePurchaseWishListDto> findPurchaseWishMember(int productId) {

		boolean productExists = productsRepository.existsById(productId);
		if (!productExists) {
			throw new BusinessException("Invalid productId " + productId);
		}

		// 구매 희망자 엔티티 리스트
		List<PurchaseWishEntity> purchaseWishEntities =
				purchaseWishRepository.findAllByProductId(productId);
		System.out.println("Entity List is " + purchaseWishEntities);

		// memberId 리스트
		List<Integer> memberIds =
				purchaseWishEntities.stream().map(PurchaseWishEntity::getMemberId).toList();
		System.out.println("MemberId list is " + memberIds);

		// memberDto
		List<ResponsePurchaseWishListDto> result =
				memberJpaRepository.findAllByMemberIdIn(memberIds).stream()
						.map(member -> new ResponsePurchaseWishListDto(member.getMemberId(), member.getId()))
						.toList();
		System.out.println("Result is " + result);

		return result;
	}
}
