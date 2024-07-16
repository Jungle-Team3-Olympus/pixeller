package com.jungle.navigation.product.service;

import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.product.entity.PurchaseWishEntity;
import com.jungle.navigation.product.repository.ProductsRepository;
import com.jungle.navigation.product.repository.PurchaseWishRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PurchaseWishService {

	private final PurchaseWishRepository purchaseWishRepository;
	private final ProductsRepository productsRepository;

	public PurchaseWishService(
			PurchaseWishRepository purchaseWishRepository, ProductsRepository productsRepository) {
		this.purchaseWishRepository = purchaseWishRepository;
		this.productsRepository = productsRepository;
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
}
