package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {}
