package gs.s3object.service.CephS3.impl;

import gs.s3object.service.CephS3.CephS3Service;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CephS3ServiceImpl implements CephS3Service {
	private final S3Client s3Client;
	private final S3Presigner presigner;
	
	@Value("${ceph.s3.bucket}")
	private String bucket;
	
	public URL generateUploadUrl(String key, String contentType, int expireSeconds) {
		String safeType = (contentType == null || contentType.isBlank())
				                  ? "application/octet-stream"
				                  : contentType;
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				                                    .bucket(bucket)
				                                    .key(key)
				                                    .contentType(safeType)
				                                    .build();
		
		PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(
				builder -> builder
						           .signatureDuration(Duration.ofSeconds(expireSeconds))
						           .putObjectRequest(putObjectRequest)
		);
		
		return presignedRequest.url();
	}
	
	public URL generateDownloadUrl(String key, int expireSeconds) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				                                    .bucket(bucket)
				                                    .key(key)
				                                    .build();
		
		PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
				builder -> builder
						           .signatureDuration(Duration.ofSeconds(expireSeconds))
						           .getObjectRequest(getObjectRequest)
		);
		
		return presignedRequest.url();
	}
	
	public void uploadFile(String key, byte[] bytes, String contentType) {
		PutObjectRequest request = PutObjectRequest.builder()
				                           .bucket(bucket)
				                           .key(key)
				                           .contentType(contentType)
				                           .build();
		s3Client.putObject(request, RequestBody.fromBytes(bytes));
	}
}
