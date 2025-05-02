package ru.hpclab.hl.module1.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.cache.DoctorRedisCache;
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

    private final DoctorRedisCache doctorRedisCache;

    public DoctorClient(RestTemplate restTemplate, ObservabilityService observabilityService, DoctorRedisCache doctorRedisCache) {
        this.restTemplate = restTemplate;
        this.observabilityService = observabilityService;
        this.doctorRedisCache = doctorRedisCache;
    }

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

    public DoctorDTO getDoctorById(Long id) {
        observabilityService.start("doctorClient.getById");
        try {
            return doctorRedisCache.get(id).orElseGet(() -> {
                String url = "http://" + mainServiceHost + ":" + mainServicePort + "/doctors/" + id;
                DoctorDTO doctor = restTemplate.getForObject(url, DoctorDTO.class);
                doctorRedisCache.put(doctor);
                return doctor;
            });
        } finally {
            observabilityService.stop("doctorClient.getById");
        }
    }
}
