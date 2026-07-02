package rocks.ifa.spring.auth.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rocks.ifa.spring.auth.dtos.LineTokenPayload;
import rocks.ifa.spring.auth.port.LineAuthPort;

import java.util.Optional;

@Slf4j
@Component("lineAuthAdapter")
public class LineAuthAdapter implements LineAuthPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";

    @Override
    public Optional<LineTokenPayload> verifyIdToken(String idToken, String clientId) {
        // 1. Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. Set up the request body (form data)
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id_token", idToken);
        map.add("client_id", clientId);

        // 3. Create the HTTP entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            // 4. Send the POST request
            log.info("Verifying ID token with LINE API...");
            LineTokenPayload payload = restTemplate.postForObject(LINE_VERIFY_URL, request, LineTokenPayload.class);
            log.info("Successfully verified ID token. LINE User Sub: {}", payload != null ? payload.sub() : "null");
            return Optional.ofNullable(payload);
        } catch (HttpClientErrorException e) {
            // 5. Log detailed error information from LINE
            log.error("Error while verifying LINE ID token. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            // Catch other potential exceptions (e.g., network issues)
            log.error("An unexpected error occurred during LINE ID token verification.", e);
            return Optional.empty();
        }
    }
}
