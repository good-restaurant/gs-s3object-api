//package gs.s3object.swagger;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//
//@Configuration
//public class SwaggerConfig {
//
////    @Value("${server.port}")
////    private String serverPort;
////
////    @Value("${server.ssl.enabled:false}")
////    private boolean isSslEnabled;
////
////    @Value("${swagger.host:localhost}")
////    private String swaggerHost;
//
//    @Bean
//    public OpenAPI openAPI() {
//
//        // 서버 1: 로컬 개발 환경
//        Server localServer = new Server()
//                .url("http://localhost:12390") // 로컬 서버 URL
//                .description("Local Development Server");
//
//        // 서버 2: 프로덕션 환경
//        Server productionServer = new Server()
//                .url("https://mes-api.i4624.info") // 프로덕션 서버 URL
//                .description("Production Server");
//
//
//        // 기본 정보 설정
//        Info info = new Info()
//                .version("v0.2.1")
//                .title("MES API")
//                .description("웹 프로젝트 API with OAUTH2 - MES API");
//
//        // JWT 인증 스키마 설정
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
//
//        // OpenAPI 구성
//        return new OpenAPI()
//                .info(info)
//                .servers(Arrays.asList(localServer, productionServer)) // 여러 서버 추가
//                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme)) // 보안 스키마 추가
//                .addSecurityItem(securityRequirement); // 보안 요구사항 추가
//    }
//}
//
//
//
