package rocks.ifa.spring.config;

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
        // Define the production server URL
        Server productionServer = new Server();
        productionServer.setUrl("https://ifa.rocks");
        productionServer.setDescription("Production Server");

        // Define the local development server URL
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        return new OpenAPI()
                .info(new Info()
                        .title("ifa.rocks API")
                        .version("v1.0.0")
                        .description("API for ifa.rocks")
                        .termsOfService("https://ifa.rocks/terms")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                )
                // Add the server list to the OpenAPI definition
                .servers(List.of(productionServer, localServer));
    }
}
