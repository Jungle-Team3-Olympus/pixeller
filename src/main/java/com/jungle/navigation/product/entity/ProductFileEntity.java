package com.jungle.navigation.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "file")
public class ProductFileEntity {
	private static final char SELLING_PRODUCT_IMAGE = 'y';
	private static final char NOT_SELLING_PRODUCT_IMAGE = 'n';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private int fileId;

	@Builder.Default
	@Column(name = "use_yn")
	private char useYn = SELLING_PRODUCT_IMAGE;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_path", columnDefinition = "TEXT")
	private String filePath;

	private String extension;

	@Column(name = "product_id")
	private int productId;
}
