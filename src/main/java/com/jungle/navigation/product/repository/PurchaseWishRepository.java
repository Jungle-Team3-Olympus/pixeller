package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.PurchaseWishEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseWishRepository extends JpaRepository<PurchaseWishEntity, Integer> {
	List<PurchaseWishEntity> findAllByProductId(int productId);
}
