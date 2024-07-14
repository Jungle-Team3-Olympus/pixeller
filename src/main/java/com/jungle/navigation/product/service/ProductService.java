package com.jungle.navigation.product.service;

import com.jungle.navigation.member.MemberEntity;
import com.jungle.navigation.member.MemberJpaRepository;
import com.jungle.navigation.product.dto.DetailDto;
import com.jungle.navigation.product.dto.ProductDto;
import com.jungle.navigation.product.entity.ProductEntity;
import com.jungle.navigation.product.entity.ProductFileEntity;
import com.jungle.navigation.product.repository.FileRepository;
import com.jungle.navigation.product.repository.ProductsRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public ProductEntity registerProduct(
			ProductDto productDto, List<MultipartFile> imgFiles, Long memberId) {
		Date createdAt = Timestamp.valueOf(LocalDateTime.now());

		ProductEntity newProduct = new ProductEntity();
		newProduct.setMemberId(memberId);
		newProduct.setName(productDto.name());
		newProduct.setCategory(productDto.category());
		newProduct.setPrice(productDto.price());
		newProduct.setDescription(productDto.description());
		newProduct.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
		newProduct.setSale_yn('y');
		newProduct.setUse_yn('y');

		newProduct = productsRepository.save(newProduct);

		List<ProductFileEntity> productFiles = saveProductImages(imgFiles, newProduct.getProductId());
		fileRepository.saveAll(productFiles);

		return newProduct;
	}

	// 상품 수정하기
	@Transactional
	public ProductEntity editorProduct(ProductDto productDto, List<MultipartFile> imgFiles, int id) {
		ProductEntity existingProduct =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		existingProduct.setName(productDto.name());
		existingProduct.setCategory(productDto.category());
		existingProduct.setPrice(productDto.price());
		existingProduct.setDescription(productDto.description());

		existingProduct = productsRepository.save(existingProduct);

		// 기존 이미지 파일 use_yn 속성을 'n'으로 변경
		List<ProductFileEntity> existingFiles =
				fileRepository.findAllByProductId(existingProduct.getProductId());
		for (ProductFileEntity file : existingFiles) {
			file.setUse_yn('n');
		}
		fileRepository.saveAll(existingFiles);

		List<ProductFileEntity> productFiles =
				saveProductImages(imgFiles, existingProduct.getProductId());
		fileRepository.saveAll(productFiles);

		return existingProduct;
	}

	// 상품 삭제(use_yn -> n)
	@Transactional
	public void deleteProduct(int id) {
		ProductEntity existingProduct =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		existingProduct.setUse_yn('n');
		productsRepository.save(existingProduct);

		List<ProductFileEntity> productFiles = fileRepository.findAllByProductId(id);
		for (ProductFileEntity file : productFiles) {
			file.setUse_yn('n');
		}
		fileRepository.saveAll(productFiles);
	}

	// 모든 상품 조회(이미지 포함)
	@Transactional(readOnly = true)
	public List<DetailDto> getAllProducts() {
		List<ProductEntity> products = productsRepository.findAll();
		List<DetailDto> AllProductWithImage = new ArrayList<>();

		for (ProductEntity product : products) {
			int productId = product.getProductId();
			AllProductWithImage.add(getProductWithImageUrlById(productId));
		}
		return AllProductWithImage;
	}

	// 특정 상품 조회(서비스)
	@Transactional(readOnly = true)
	public DetailDto getProductWithImageUrlById(int id) {
		ProductEntity product =
				productsRepository
						.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found"));

		List<ProductFileEntity> fileEntities = fileRepository.findByProductId(id);
		MemberEntity member =
				memberJpaRepository
						.findById(product.getMemberId())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		DetailDto.MemberDto memberDto =
				new DetailDto.MemberDto(
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

		// 이미지 파일경로 가져오기
		List<String> fileUrls =
				fileEntities.stream()
						.map(ProductFileEntity::getFilePath) // Assuming filePath is the URL
						.toList();

		DetailDto productWithFilesDTO = getDetailDto(product, fileUrls, memberDto);

		return productWithFilesDTO;
	}

	private static DetailDto getDetailDto(
			ProductEntity product, List<String> fileUrls, DetailDto.MemberDto memberDto) {
		DetailDto productWithFilesDTO = new DetailDto();

		productWithFilesDTO.setProductId(product.getProductId());
		productWithFilesDTO.setImageFileUrls(fileUrls);
		productWithFilesDTO.setMemberDto(memberDto);
		productWithFilesDTO.setName(product.getName());
		productWithFilesDTO.setCategory(product.getCategory());
		productWithFilesDTO.setPrice(product.getPrice());
		productWithFilesDTO.setDescription(product.getDescription());
		productWithFilesDTO.setSaleYn(product.getSale_yn());
		productWithFilesDTO.setUseYn(product.getUse_yn());
		return productWithFilesDTO;
	}

	private List<ProductFileEntity> saveProductImages(List<MultipartFile> images, int productId) {
		List<ProductFileEntity> productFiles = new ArrayList<>();
		Date createAt = Timestamp.valueOf(LocalDateTime.now());
		String uploadDir = "src/main/resources/static/images/";
		Path uploadPath = Paths.get(uploadDir).normalize().toAbsolutePath();

		try {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			for (MultipartFile image : images) {
				String originalFileName = image.getOriginalFilename();
				// 정리된 파일 네임
				String sanitizedFileName = sanitizeFileName(createAt.getTime() + "_" + originalFileName);
				Path filePath = uploadPath.resolve(sanitizedFileName).normalize();

				// 업로드 경로가 지정된 업로드 디렉토리 내에 있는지 확인
				if (!filePath.startsWith(uploadPath)) {
					throw new SecurityException("Invalid file path: " + filePath);
				}

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}

				ProductFileEntity newImageFile = new ProductFileEntity();
				newImageFile.setUse_yn('y');
				newImageFile.setFileName(originalFileName);
				newImageFile.setFilePath(filePath.toString());
				newImageFile.setExtension(image.getContentType());
				newImageFile.setProductId(productId);

				productFiles.add(newImageFile);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Exception: " + ex.getMessage());
		}

		return productFiles;
	}

	// 파일 이름에서 알파벳, 숫자, 점(.), 대시(-)를 제외한 모든 문자를 언더스코어(_)로 대체
	private String sanitizeFileName(String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
	}

	@Transactional(readOnly = true)
	public List<ProductFileEntity> getProductFilesByProductId(int productId) {
		return fileRepository.findByProductId(productId);
	}
}
