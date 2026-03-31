package rocks.ifa.spring.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "line.liff")
public class LineLiffProperties {
    private String channelId;
}
