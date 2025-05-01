package ru.hpclab.hl.module1.client;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CrashClient {

    private final RestTemplate restTemplate;

    @Value("${main.service.host}")
    private String mainServiceHost;

    @Value("${main.service.port}")
    private String mainServicePort;

    @Retry(name = "MAIN_SERVICE")
    public void crashCore() {
        String url = "http://" + mainServiceHost + ":" + mainServicePort + "/internal/crash";
        try {
            restTemplate.postForEntity(url, null, Void.class);
            System.out.println("Crash signal sent to core service.");
        } catch (Exception e) {
            System.err.println("Failed to crash core service: " + e.getMessage());
        }
    }
}
