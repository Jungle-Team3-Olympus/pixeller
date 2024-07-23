package com.jungle.navigation.product.controller;

import com.jungle.navigation.product.dto.DataResponseDto;
import com.jungle.navigation.product.service.FileService;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presigned-url")
public class FileController {

	private final FileService fileService;

	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@GetMapping("{fileName}")
	public DataResponseDto<Map<String, String>> getPresignedUrl(
			@PathVariable(name = "fileName") @Schema(description = "확장자명을 포함해주세요") String fileName) {
		return DataResponseDto.from(fileService.getPresignedUrl("images", fileName));
	}
}
