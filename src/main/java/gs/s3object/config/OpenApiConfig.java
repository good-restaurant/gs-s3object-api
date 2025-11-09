package gs.s3object.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI openAPI() {
		
		// ✅ 서버 프로필 목록 정의
		Server localServer = new Server()
				                     .url("http://localhost:8088")
				                     .description("Local development server");
		
		Server stagingServer = new Server()
				                       .url("https://dev.naver.i4624.info")
				                       .description("HTTPS 테스트 Server");
		
		Server productionServer = new Server()
				                          .url("https://s3api.i4624.info")
				                          .description("배포 환경용 HTTPS Server");
		
		// ✅ API 정보
		Info info = new Info()
				            .title("S3 Object API")
				            .version("v0.0.1")
				            .description("CEPH S3 Object Storage API for presigned URL management");
		
		// ✅ JWT bearer 인증 정의 (선택 사항)
		SecurityScheme securityScheme = new SecurityScheme()
				                                .type(SecurityScheme.Type.HTTP)
				                                .scheme("bearer")
				                                .bearerFormat("JWT")
				                                .in(SecurityScheme.In.HEADER)
				                                .name("Authorization");
		
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
		
		// ✅ OpenAPI 본체 반환
		return new OpenAPI()
				       .info(info)
				       .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
				       .addSecurityItem(securityRequirement)
				       .servers(List.of(localServer, stagingServer, productionServer)); // ← 서버 프로필 등록
	}
}