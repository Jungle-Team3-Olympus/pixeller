package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {

	List<ProductEntity> findAllByUseYnAndSaleYn(char use_yn, char sale_yn);
}
