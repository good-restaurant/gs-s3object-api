package gs.s3object.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class CephS3V2Config {
	
	@Value("${ceph.s3.endpoint}")
	private String endpoint;
	
	@Value("${ceph.s3.access-key}")
	private String accessKey;
	
	@Value("${ceph.s3.secret-key}")
	private String secretKey;
	
	@Value("${ceph.s3.region}")
	private String region;
	
	@Bean
	public S3Client s3Client() {
		S3Configuration s3Config = S3Configuration.builder()
				                           .pathStyleAccessEnabled(true) // Ceph/MinIO 필수
				                           .build();
		
		return S3Client.builder()
				       .credentialsProvider(StaticCredentialsProvider.create(
						       AwsBasicCredentials.create(accessKey, secretKey)))
				       .endpointOverride(URI.create(endpoint))
				       .region(Region.of(region))
				       .serviceConfiguration(s3Config)
				       .httpClientBuilder(UrlConnectionHttpClient.builder())
				       .build();
	}
	
	@Bean
	public S3Presigner s3Presigner() {
		return S3Presigner.builder()
				       .credentialsProvider(StaticCredentialsProvider.create(
						       AwsBasicCredentials.create(accessKey, secretKey)))
				       .endpointOverride(URI.create(endpoint))
				       .region(Region.of(region))
				       .serviceConfiguration(S3Configuration.builder()
						                             .pathStyleAccessEnabled(true)
						                             .build())
				       .build();
	}
}
