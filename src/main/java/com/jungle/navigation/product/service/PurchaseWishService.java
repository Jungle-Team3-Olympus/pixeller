package com.jungle.navigation.product.service;

import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.member.MemberJpaRepository;
import com.jungle.navigation.product.dto.ResponsePurchaseWishListDto;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.repository.ProductsRepository;
import com.jungle.navigation.product.repository.PurchaseWishRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PurchaseWishService {

	private final PurchaseWishRepository purchaseWishRepository;
	private final ProductsRepository productsRepository;
	private final MemberJpaRepository memberJpaRepository;

	public PurchaseWishService(
			PurchaseWishRepository purchaseWishRepository,
			ProductsRepository productsRepository,
			MemberJpaRepository memberJpaRepository) {
		this.purchaseWishRepository = purchaseWishRepository;
		this.productsRepository = productsRepository;
		this.memberJpaRepository = memberJpaRepository;
	}

	@Transactional
	public PurchaseWishEntity savePurchaseWish(int productId, int memberId) {

		boolean productExists = productsRepository.existsById(productId);
		if (!productExists) {
			throw new BusinessException("Invalid productId " + productId);
		}

		PurchaseWishEntity purchaseWish = new PurchaseWishEntity();
		purchaseWish.setMemberId(Math.toIntExact(memberId));
		purchaseWish.setProductId(productId);

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
