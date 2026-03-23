package rocks.ifa.spring;

import rocks.ifa.spring.config.AppProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main entry point for the Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@MapperScan("rocks.ifa.spring.mapper")
public class CalculatorApiSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApiSpringApplication.class, args);
	}
}
