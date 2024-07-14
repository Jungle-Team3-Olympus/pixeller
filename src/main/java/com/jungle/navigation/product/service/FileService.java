package com.jungle.navigation.product.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${cloud.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	// Presigned URL 발급
	public Map<String, String> getPresignedUrl(String prefix, String fileName) {
		if (!prefix.isEmpty()) {
			fileName = createPath(prefix, fileName);
		}

		GeneratePresignedUrlRequest generatePresignedUrlRequest =
				getGeneratePresignedUrlRequest(bucket, fileName);
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

		return Map.of("url", url.toString());
	}

	// 파일 업로드용 Presigned URL 생성
	private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(
			String bucket, String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
				new GeneratePresignedUrlRequest(bucket, fileName)
						.withMethod(HttpMethod.PUT)
						.withExpiration(getPresignedUrlExpiration());

		generatePresignedUrlRequest.addRequestParameter(
				Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

		return generatePresignedUrlRequest;
	}

	// Presigned URL의 유효 기간 설정
	private Date getPresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);

		return expiration;
	}

	// UUID를 사용하여 파일 고유 ID 생성
	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	// createPath 파일의 전체 경로 생성
	private String createPath(String prefix, String fileName) {
		String fileId = createFileId();
		return String.format("%s/%s", prefix, fileId + "-" + fileName);
	}
}
