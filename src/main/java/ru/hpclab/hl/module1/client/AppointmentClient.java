package ru.hpclab.hl.module1.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.AppointmentDTO;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;

@Service
public class AppointmentClient {

    private final RestTemplate restTemplate;
    private final ObservabilityService observabilityService;

    @Value("${main.service.host}")
    private String mainServiceHost;

    @Value("${main.service.port}")
    private String mainServicePort;

    public AppointmentClient(RestTemplate restTemplate, ObservabilityService observabilityService) {
        this.restTemplate = restTemplate;
        this.observabilityService = observabilityService;
    }

    public List<AppointmentDTO> getAppointments() {
        observabilityService.start("appointmentClient.getAll");
        try {
            String url = "http://" + mainServiceHost + ":" + mainServicePort + "/appointments";

            ResponseEntity<List<AppointmentDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            return response.getBody();
        } finally {
            observabilityService.stop("appointmentClient.getAll");
        }
    }
}
