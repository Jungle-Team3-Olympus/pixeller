package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.AuctionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Integer> {

	List<AuctionEntity> findAllByAuctionId(int auctionId);

	List<AuctionEntity> findAllByProductId(int productId);
}
