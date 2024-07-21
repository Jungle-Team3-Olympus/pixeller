package com.jungle.navigation.product.service;

import com.jungle.navigation.common.exception.BusinessException;
import com.jungle.navigation.member.MemberEntity;
import com.jungle.navigation.member.MemberJpaRepository;
import com.jungle.navigation.product.dto.RequestEditDto;
import com.jungle.navigation.product.dto.RequestProductDto;
import com.jungle.navigation.product.dto.RequestSellerCheckDto;
import com.jungle.navigation.product.dto.ResponseProductWithImageUrlDto;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.entity.ProductFileEntity;
import com.jungle.navigation.product.repository.FileRepository;
import com.jungle.navigation.product.repository.ProductsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
		ProductEntity newProduct = new ProductEntity();
		newProduct.setMemberId(memberId);
		newProduct.setName(requestProductDto.name());
		newProduct.setPrice(requestProductDto.price());
		newProduct.setDescription(requestProductDto.description());

		newProduct = productsRepository.save(newProduct);
		saveImage(requestProductDto.files(), newProduct.getProductId());
		return newProduct;
	}

	// S3에 저장하는 이미지 파일 정보(fileName, filePath, productId, use_yn) 저장 메서드
	private void saveImage(List<RequestProductDto.File> files, int productId) {

		boolean productExists = productsRepository.existsById(productId);
		if (!productExists) {
			throw new BusinessException("Invalid productId " + productId);
		}

		List<ProductFileEntity> fileEntities = getFileEntities(files, productId);
		fileRepository.saveAll(fileEntities);
	}

	private static List<ProductFileEntity> getFileEntities(
			List<RequestProductDto.File> files, int productId) {
		List<ProductFileEntity> fileEntities = new ArrayList<>();
		String basePath = "https://product-image-kfc.s3.ap-northeast-2.amazonaws.com/images/";
		for (RequestProductDto.File file : files) {
			if (!file.path().startsWith(basePath)) {
				throw new BusinessException("Invalid image file path");
			}
			ProductFileEntity newFile = new ProductFileEntity();
			newFile.setFileName(file.filename());
			newFile.setFilePath(file.path());
			newFile.setProductId(productId);
			fileEntities.add(newFile);
		}
		return fileEntities;
	}

	// 상품 수정하기
	@Transactional
	public ProductEntity editorProduct(RequestEditDto requestEditDto, int productId) {

		boolean productExists = productsRepository.existsById(productId);
		if (!productExists) {
			throw new BusinessException("Invalid productId " + productId);
		}

		ProductEntity existingProduct =
				productsRepository
						.findById(productId)
						.orElseThrow(() -> new BusinessException("Product not found"));

		existingProduct.setName(requestEditDto.name());
		existingProduct.setPrice(requestEditDto.price());
		existingProduct.setDescription(requestEditDto.description());

		existingProduct = productsRepository.save(existingProduct);

		// 기존 이미지 파일 use_yn 속성을 'n'으로 변경
		List<ProductFileEntity> existingFiles =
				fileRepository.findAllByProductIdAndUseYn(productId, 'y');
		for (ProductFileEntity file : existingFiles) {
			file.setUseYn('n');
		}
		fileRepository.saveAll(existingFiles);

		saveImage(requestEditDto.files(), productId);

		// 수정된 파일 정보 반환
		return existingProduct;
	}

	// 상품 삭제(use_yn -> n)
	@Transactional
	public void deleteProduct(int productId) {

		boolean productExists = productsRepository.existsById(productId);
		if (!productExists) {
			throw new BusinessException("Invalid productId " + productId);
		}

		ProductEntity existingProduct =
				productsRepository
						.findById(productId)
						.orElseThrow(() -> new BusinessException("Product not found"));

		existingProduct.setUseYn('n');
		productsRepository.save(existingProduct);

		List<ProductFileEntity> productFiles =
				fileRepository.findAllByProductIdAndUseYn(productId, 'y');
		for (ProductFileEntity file : productFiles) {
			file.setUseYn('n');
		}
		fileRepository.saveAll(productFiles);
	}

	// 모든 상품 조회(이미지 포함)
	@Transactional(readOnly = true)
	public List<ResponseProductWithImageUrlDto> getAllProducts() {
		List<ProductEntity> products = productsRepository.findAllByUseYnAndSaleYn('y', 'n');

		return products.stream()
				.map(product -> getProductWithImageUrlById(product.getProductId()))
				.toList();
	}

	@Transactional(readOnly = true)
	public ResponseProductWithImageUrlDto getProductWithImageUrlById(int productId) {
		// 상품 존재 확인
		ProductEntity product =
				productsRepository
						.findById(productId)
						.orElseThrow(() -> new BusinessException("Invalid productId " + productId));

		// 상품에 속한 이미지 파일 엔티티들 가져오기
		List<ProductFileEntity> fileEntities =
				fileRepository.findAllByProductIdAndUseYn(productId, 'y');

		// 이미지 파일 경로들 리스트로 변환
		List<String> fileUrls =
				fileEntities.stream()
						.map(ProductFileEntity::getFilePath) // Assuming filePath is the URL
						.toList();

		// 상품 등록자 정보 가져오기
		MemberEntity member =
				memberJpaRepository
						.findById(product.getMemberId())
						.orElseThrow(() -> new BusinessException("존재하지 않는 유저입니다."));

		// 상품과 관련된 멤버 정보 DTO 생성
		ResponseProductWithImageUrlDto.MemberDto memberDto =
				new ResponseProductWithImageUrlDto.MemberDto(
						product.getMemberId(),
						member.getId(),
						member.getUserType(),
						member.getX(),
						member.getY(),
						member.getLastLogin(),
						member.getJoinedAt(),
						member.getUsername(),
						member.getEmail(),
						member.getDirection(),
						member.getGoogleIdentity());

		// Response DTO 생성
		return getResponseProductWithImageUrlDto(product, fileUrls, memberDto);
	}

	private ResponseProductWithImageUrlDto getResponseProductWithImageUrlDto(
			ProductEntity product,
			List<String> fileUrls,
			ResponseProductWithImageUrlDto.MemberDto memberDto) {
		ResponseProductWithImageUrlDto responseProductWithImageUrlDto =
				new ResponseProductWithImageUrlDto();
		responseProductWithImageUrlDto.setProductId(product.getProductId());
		responseProductWithImageUrlDto.setImageFileUrls(fileUrls);
		responseProductWithImageUrlDto.setName(product.getName());
		responseProductWithImageUrlDto.setPrice(product.getPrice());
		responseProductWithImageUrlDto.setDescription(product.getDescription());
		responseProductWithImageUrlDto.setSaleYn(product.getSaleYn());
		responseProductWithImageUrlDto.setUseYn(product.getUseYn());
		responseProductWithImageUrlDto.setMemberDto(memberDto);
		return responseProductWithImageUrlDto;
	}

	// api 접근 유저가 판매자가 맞는지 체크
	public boolean checkUserEqualSeller(RequestSellerCheckDto requestSellerCheckDto) {

		ProductEntity product =
				productsRepository
						.findById(requestSellerCheckDto.productId())
						.orElseThrow(() -> new BusinessException("Invalid product"));

		MemberEntity member =
				memberJpaRepository
						.findById(product.getMemberId())
						.orElseThrow(() -> new BusinessException("Invalid member"));

		String sellerId = member.getId();

		return Objects.equals(sellerId, requestSellerCheckDto.id());
	}

	public List<ResponseProductWithImageUrlDto> getProductByMember(Long memberId) {
		return productsRepository.findByMemberId(memberId).stream()
				.map(product -> getProductWithImageUrlById(product.getProductId()))
				.toList();
	}
}
