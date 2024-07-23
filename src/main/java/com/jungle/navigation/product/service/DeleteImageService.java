package com.jungle.navigation.product.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteImageService {
	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	public void deleteFile(String fileName) {

		log.info("Delete file: " + fileName);
		amazonS3.deleteObject(bucket, fileName);
	}
}
