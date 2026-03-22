package com.en_chu.calculator_api_spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 定義生產環境的 Server URL
        Server productionServer = new Server();
        productionServer.setUrl("https://calculator-api-spring-592400229145.asia-east1.run.app");
        productionServer.setDescription("Production Cloud Run Server");

        // 定義本地開發環境的 Server URL
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8888");
        localServer.setDescription("Local Development Server");

        return new OpenAPI()
                .info(new Info()
                        .title("財務計算機 API (Financial Calculator API)")
                        .version("v1.0.0")
                        .description("這是一個開源的財務計算機後端 API，提供個人理財檔案管理與複利計算等功能。")
                        .termsOfService("https://www.en-chu.com/terms")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                )
                // 將 Server 列表加入 OpenAPI 定義中
                .servers(List.of(productionServer, localServer));
    }
}
