package com.jungle.navigation.product.service;

import com.jungle.navigation.alarm.event.DelayAlarmEvent;
import com.jungle.navigation.alarm.event.WishPurchaseEvent;
import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.repository.ProductsRepository;
import com.jungle.navigation.product.repository.PurchaseWishRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PurchaseWishService {

	private final PurchaseWishRepository purchaseWishRepository;
	private final ProductsRepository productsRepository;
	private final ApplicationEventPublisher eventPublisher;

	public PurchaseWishService(
			PurchaseWishRepository purchaseWishRepository,
			ProductsRepository productsRepository,
			ApplicationEventPublisher eventPublisher) {
		this.purchaseWishRepository = purchaseWishRepository;
		this.productsRepository = productsRepository;
		this.eventPublisher = eventPublisher;
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
				new DelayAlarmEvent(
						Long.valueOf(memberId), Long.valueOf(productId), productEntity.getAuctionStartTime()));
		eventPublisher.publishEvent(
				new WishPurchaseEvent(
						Long.valueOf(productEntity.getMemberId()), Long.valueOf(Long.valueOf(productId))));

		return purchaseWishRepository.save(purchaseWish);
	}
}
