package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.ProductFileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<ProductFileEntity, Integer> {
	List<ProductFileEntity> findByProductId(int productId);

	List<ProductFileEntity> findAllByProductId(int productId);
}
