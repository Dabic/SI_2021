package rs.raf.modelresolver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.modelresolver.dto.CredentialsHolder;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class ServiceInitializer {

    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServiceInitializer.class);

    @Value("classpath:service-info.json")
    private Resource serviceDetails;

    @Value("${broker.api.username}")
    private String username;

    @Value("${broker.api.password}")
    private String password;

    public ServiceInitializer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() throws IOException {
        String serviceInfo = new String(Files.readAllBytes(serviceDetails.getFile().toPath()));
        HttpEntity<String> request = new HttpEntity<>(serviceInfo, getAuthHeaders());
        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity("http://localhost:8080/api/services", request, String.class);
            logger.info("Registration to broker successful!");
        } catch (HttpClientErrorException e) {
            logger.info("Registration to broker unsuccessful! Exiting...");
            System.exit(0);
        }
    }

    public HttpHeaders getAuthHeaders() {
        CredentialsHolder credentialsHolder = new CredentialsHolder();
        credentialsHolder.setUsername(username);
        credentialsHolder.setPassword(password);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/users/login",
                    credentialsHolder,
                    String.class);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", response.getHeaders().get("Authorization").get(0));
            httpHeaders.add("Content-Type", "application/json");
            return httpHeaders;
        } catch (HttpClientErrorException e) {
            logger.error("Unsuccessful login to broker! System exiting...");
            System.exit(0);
        }
        return null;
    }
}
