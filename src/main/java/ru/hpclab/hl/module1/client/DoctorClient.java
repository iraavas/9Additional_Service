package ru.hpclab.hl.module1.client;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.cache.DoctorCache;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorClient {

    private final RestTemplate restTemplate;
    private final ObservabilityService observabilityService;

    @Value("${main.service.host}")
    private String mainServiceHost;

    @Value("${main.service.port}")
    private String mainServicePort;

    public DoctorClient(RestTemplate restTemplate, ObservabilityService observabilityService) {
        this.restTemplate = restTemplate;
        this.observabilityService = observabilityService;
    }

    @Retry(name = "MAIN_SERVICE")
    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        observabilityService.start("doctorClient.getBySpecialization");
        try {
            String url = "http://" + mainServiceHost + ":" + mainServicePort + "/doctors";
            ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<DoctorDTO> allDoctors = response.getBody();

            return allDoctors.stream()
                    .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stop("doctorClient.getBySpecialization");
        }
    }

    @Retry(name = "MAIN_SERVICE")
    public DoctorDTO getDoctorById(Long id) {
        observabilityService.start("doctorClient.getById");
        try {
            return DoctorCache.get(id).orElseGet(() -> {
                String url = "http://" + mainServiceHost + ":" + mainServicePort + "/doctors/" + id;
                DoctorDTO doctor = restTemplate.getForObject(url, DoctorDTO.class);
                DoctorCache.put(doctor);
                return doctor;
            });
        } finally {
            observabilityService.stop("doctorClient.getById");
        }
    }
}
