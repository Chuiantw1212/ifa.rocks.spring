package com.en_chu.calculator_api_spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Cors cors = new Cors();

    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }
}
