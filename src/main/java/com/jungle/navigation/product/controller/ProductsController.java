package com.jungle.navigation.product.controller;

import com.jungle.navigation.auth.presentation.support.Member;
import com.jungle.navigation.auth.presentation.support.MemberInfo;
import com.jungle.navigation.product.dto.DetailDto;
import com.jungle.navigation.product.dto.ProductDto;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.entity.ProductFileEntity;
import com.jungle.navigation.product.service.ProductService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

	private final ProductService productService;

	public ProductsController(ProductService productService) {
		this.productService = productService;
	}

	// 상품 등록
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ProductEntity> registerProduct(
			ProductDto productDto,
			@RequestPart(value = "imgFiles", required = true) List<MultipartFile> imgFiles,
			BindingResult result,
			@Member MemberInfo memberInfo) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		ProductEntity newProduct =
				productService.registerProduct(productDto, imgFiles, memberInfo.getMemberId());
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

	// 모든 상품 조회
	@GetMapping
	public ResponseEntity<List<DetailDto>> getAllProducts() {
		List<DetailDto> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	// 특정 상품 조회
	@GetMapping("/{productId}")
	public ResponseEntity<DetailDto> getProductById(@PathVariable("productId") int productId) {
		DetailDto productWithImageUrls = productService.getProductWithImageUrlById(productId);
		return ResponseEntity.ok(productWithImageUrls);
	}

	// 상품 수정
	@PutMapping("/{productId}")
	public ResponseEntity<ProductEntity> updateProduct(
			@PathVariable("productId") int productId,
			ProductDto productDto,
			@RequestPart(value = "imgFiles", required = true) List<MultipartFile> imgFiles,
			BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		ProductEntity updatedProduct = productService.editorProduct(productDto, imgFiles, productId);
		if (updatedProduct != null) {
			return ResponseEntity.ok(updatedProduct);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.noContent().build();
	}

	// 특정 상품의 이미지 파일들 조회
	@GetMapping("/{productId}/files")
	public ResponseEntity<List<ProductFileEntity>> getProductFiles(
			@PathVariable("productId") int productId) {
		List<ProductFileEntity> productFiles = productService.getProductFilesByProductId(productId);
		if (!productFiles.isEmpty()) {
			return ResponseEntity.ok(productFiles);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
