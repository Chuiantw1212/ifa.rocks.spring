package rocks.ifa.spring.domain.auth.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
// Corrected import
import rocks.ifa.spring.domain.line.LineTokenPayload;
import rocks.ifa.spring.domain.auth.port.LineAuthPort;

import java.util.Optional;

@Slf4j
@Component("lineAuthAdapter")
public class LineAuthAdapter implements LineAuthPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";
    private static final String LINE_CHANNEL_ID = "2009612107";

    @Override
    public Optional<LineTokenPayload> verifyIdToken(String idToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id_token", idToken);
        map.add("client_id", LINE_CHANNEL_ID);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            LineTokenPayload payload = restTemplate.postForObject(LINE_VERIFY_URL, request, LineTokenPayload.class);
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
