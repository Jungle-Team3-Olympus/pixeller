package com.jungle.navigation.product.repository;

import com.jungle.navigation.product.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {

	List<ProductEntity> findAllByUseYnAndSaleYn(char use_yn, char sale_yn);

	@Query(
			"select p from ProductEntity p where p.memberId =:memberId and p.useYn='y' and p.saleYn='n'")
	List<ProductEntity> findByMemberId(@Param("memberId") Long memberId);
}
