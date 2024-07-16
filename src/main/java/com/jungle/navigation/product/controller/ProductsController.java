package com.jungle.navigation.product.controller;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.product.dto.RequestEditDto;
import com.jungle.navigation.product.dto.RequestProductDto;
import com.jungle.navigation.product.dto.ResponseProductWithImageUrlDto;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

	private final ProductService productService;

	public ProductsController(ProductService productService) {
		this.productService = productService;
	}

	// 상품 등록
	@PostMapping
	public ResponseEntity<ProductEntity> registerProduct(
			@RequestBody @Valid RequestProductDto requestProductDto, @Member MemberInfo memberInfo) {

		ProductEntity newProduct =
				productService.registerProduct(requestProductDto, memberInfo.getMemberId());
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

	// 모든 상품 조회
	@GetMapping
	public ResponseEntity<List<ResponseProductWithImageUrlDto>> getAllProducts() {
		List<ResponseProductWithImageUrlDto> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	// 특정 상품 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ResponseProductWithImageUrlDto> getProductById(
			@PathVariable("productId") int productId) {
		ResponseProductWithImageUrlDto productWithImageUrls =
				productService.getProductWithImageUrlById(productId);
		return ResponseEntity.ok(productWithImageUrls);
	}

	// 상품 수정
	@PutMapping("/{productId}")
	public ResponseEntity<ProductEntity> updateProduct(
			@PathVariable("productId") int productId, @RequestBody @Valid RequestEditDto requestEditDto) {

		ProductEntity updatedProduct = productService.editorProduct(requestEditDto, productId);
		return ResponseEntity.ok(updatedProduct);
	}

	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.noContent().build();
	}
}
