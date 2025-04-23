package ru.hpclab.hl.module1.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.AppointmentDTO;

import java.util.List;

@Service
public class AppointmentClient {

    private final RestTemplate restTemplate;

    @Value("${main.service.host}")
    private String mainServiceHost;

    @Value("${main.service.port}")
    private String mainServicePort;

    public AppointmentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<AppointmentDTO> getAppointments() {
        String url = "http://" + mainServiceHost + ":" + mainServicePort + "/appointments";

        ResponseEntity<List<AppointmentDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
}
