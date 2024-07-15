package com.jungle.navigation.product.service;

import com.jungle.navigation.member.MemberEntity;
import com.jungle.navigation.member.MemberJpaRepository;
import com.jungle.navigation.product.dto.RequestProductDto;
import com.jungle.navigation.product.dto.ResponseProductWithImageUrlDto;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.entity.ProductFileEntity;
import com.jungle.navigation.product.repository.FileRepository;
import com.jungle.navigation.product.repository.ProductsRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

	private final ProductsRepository productsRepository;
	private final FileRepository fileRepository;
	private final MemberJpaRepository memberJpaRepository;

	@Autowired
	public ProductService(
			ProductsRepository productsRepository,
			FileRepository fileRepository,
			MemberJpaRepository memberJpaRepository) {
		this.productsRepository = productsRepository;
		this.fileRepository = fileRepository;
		this.memberJpaRepository = memberJpaRepository;
	}

	// 상품 등록
	@Transactional
	public ProductEntity registerProduct(RequestProductDto requestProductDto, Long memberId) {
		Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

		ProductEntity newProduct = new ProductEntity();
		newProduct.setMemberId(memberId);
		newProduct.setName(requestProductDto.name());
		newProduct.setCategory(requestProductDto.category());
		newProduct.setPrice(requestProductDto.price());
		newProduct.setDescription(requestProductDto.description());
		newProduct.setAuctionStartTime(requestProductDto.auctionStartTime());
		newProduct.setCreatedAt(createdAt);
		newProduct.setSaleYn('n');
		newProduct.setUseYn('y');

		newProduct = productsRepository.save(newProduct);

		saveImage(requestProductDto, newProduct.getProductId());

		return newProduct;
	}

	// S3에 저장하는 이미지 파일 정보(fileName, filePath, productId, use_yn) 저장 메서드
	private void saveImage(RequestProductDto requestProductDto, int id) {
		List<ProductFileEntity> fileEntities = new ArrayList<>();
		for (RequestProductDto.File file : requestProductDto.files()) {
			ProductFileEntity newFile = new ProductFileEntity();
			newFile.setFileName(file.filename());
			newFile.setFilePath(file.path());
			newFile.setProductId(id);
			newFile.setUse_yn('y');
			fileEntities.add(newFile);
		}
		fileRepository.saveAll(fileEntities);
	}

	// 상품 수정하기
	@Transactional
	public ProductEntity editorProduct(RequestProductDto requestProductDto, int id) {
		ProductEntity existingProduct =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		existingProduct.setName(requestProductDto.name());
		existingProduct.setCategory(requestProductDto.category());
		existingProduct.setPrice(requestProductDto.price());
		existingProduct.setDescription(requestProductDto.description());

		existingProduct = productsRepository.save(existingProduct);

		// 기존 이미지 파일 use_yn 속성을 'n'으로 변경
		List<ProductFileEntity> existingFiles = fileRepository.findAllByProductId(id);
		for (ProductFileEntity file : existingFiles) {
			file.setUse_yn('n');
		}
		fileRepository.saveAll(existingFiles);

		saveImage(requestProductDto, id);

		// 수정된 파일 정보 반환
		return existingProduct;
	}

	// 상품 삭제(use_yn -> n)
	@Transactional
	public void deleteProduct(int id) {
		ProductEntity existingProduct =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		existingProduct.setUseYn('n');
		productsRepository.save(existingProduct);

		List<ProductFileEntity> productFiles = fileRepository.findAllByProductId(id);
		for (ProductFileEntity file : productFiles) {
			file.setUse_yn('n');
		}
		fileRepository.saveAll(productFiles);
	}

	// 모든 상품 조회(이미지 포함)
	@Transactional(readOnly = true)
	public List<ResponseProductWithImageUrlDto> getAllProducts() {
		List<ProductEntity> products = productsRepository.findAllByUseYnAndSaleYn('y', 'n');
		List<ResponseProductWithImageUrlDto> AllProductWithImage = new ArrayList<>();

		for (ProductEntity product : products) {
			int productId = product.getProductId();
			AllProductWithImage.add(getProductWithImageUrlById(productId));
		}
		return AllProductWithImage;
	}

	// 특정 상품 조회(상품 등록자 정보 포함)
	@Transactional(readOnly = true)
	public ResponseProductWithImageUrlDto getProductWithImageUrlById(int id) {

		// 받은 id로 product 가져오기
		ProductEntity product =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		// product 에 딸린 이미지 파일들 리스트에 담아두기
		List<ProductFileEntity> fileEntities = fileRepository.findByProductId(id);

		// 이미지 파일경로들 list 에 저장
		List<String> fileUrls =
				fileEntities.stream()
						.map(ProductFileEntity::getFilePath) // Assuming filePath is the URL
						.toList();

		// product 주인 member 정보 찾아두기
		MemberEntity member =
				memberJpaRepository
						.findById(product.getMemberId())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		// Response (member 정보 부분) memberDTO 에 멤버정보 저장 (순서 맞춰서?)
		ResponseProductWithImageUrlDto.MemberDto memberDto =
				new ResponseProductWithImageUrlDto.MemberDto(
						product.getMemberId(),
						member.getUserType(),
						member.getX(),
						member.getY(),
						member.getLastLogin(),
						member.getJoinedAt(),
						member.getUsername(),
						member.getEmail(),
						member.getDirection(),
						member.getGoogleIdentity());

		return getResponseProductWithImageUrlDto(product, fileUrls, memberDto);
	}

	private static @NotNull ResponseProductWithImageUrlDto getResponseProductWithImageUrlDto(
			ProductEntity product,
			List<String> fileUrls,
			ResponseProductWithImageUrlDto.MemberDto memberDto) {
		ResponseProductWithImageUrlDto responseProductWithImageUrlDto =
				new ResponseProductWithImageUrlDto();
		responseProductWithImageUrlDto.setProductId(product.getProductId());
		responseProductWithImageUrlDto.setImageFileUrls(fileUrls);
		responseProductWithImageUrlDto.setName(product.getName());
		responseProductWithImageUrlDto.setCategory(product.getCategory());
		responseProductWithImageUrlDto.setPrice(product.getPrice());
		responseProductWithImageUrlDto.setDescription(product.getDescription());
		responseProductWithImageUrlDto.setSaleYn(product.getSaleYn());
		responseProductWithImageUrlDto.setUseYn(product.getUseYn());
		responseProductWithImageUrlDto.setAuctionStartTime(product.getAuctionStartTime());
		responseProductWithImageUrlDto.setMemberDto(memberDto);
		return responseProductWithImageUrlDto;
	}
}


