package rocks.ifa.spring.infrastructure.gatewayimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rocks.ifa.spring.application.auth.port.LineAuthPort;
import rocks.ifa.spring.domain.line.LineTokenPayload;

import java.util.Optional;

@Slf4j
@Component
public class LineAuthAdapter implements LineAuthPort {

    private final RestTemplate restTemplate;
    private final String lineVerifyUrl;
    private final String lineChannelId;

    public LineAuthAdapter(
            RestTemplate restTemplate,
            @Value("${line.auth.verify-url}") String lineVerifyUrl,
            @Value("${line.auth.channel-id}") String lineChannelId
    ) {
        this.restTemplate = restTemplate;
        this.lineVerifyUrl = lineVerifyUrl;
        this.lineChannelId = lineChannelId;
    }

    @Override
    public Optional<LineTokenPayload> verifyIdToken(String idToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id_token", idToken);
        map.add("client_id", lineChannelId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            LineTokenPayload payload = restTemplate.postForObject(lineVerifyUrl, request, LineTokenPayload.class);
            log.info("Successfully verified LINE ID token.");
            return Optional.ofNullable(payload);
        } catch (HttpClientErrorException e) {
            log.error("LINE ID token verification failed. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("An unexpected error occurred during LINE ID token verification.", e);
            return Optional.empty();
        }
    }
}
