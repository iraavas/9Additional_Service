package ru.hpclab.hl.module1.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.cache.DoctorCache;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorClient {

    private final RestTemplate restTemplate;

    @Value("${main.service.host}")
    private String mainServiceHost;

    @Value("${main.service.port}")
    private String mainServicePort;

    public DoctorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        String url = "http://" + mainServiceHost + ":" + mainServicePort + "/doctors";

        ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<DoctorDTO> allDoctors = response.getBody();

        // Кешируем всех полученных врачей
        allDoctors.forEach(DoctorCache::put);

        return allDoctors.stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    public DoctorDTO getDoctorById(Long id) {
        // Сначала проверяем кеш
        return DoctorCache.get(id).orElseGet(() -> {
            // Если нет в кеше — делаем запрос
            String url = "http://" + mainServiceHost + ":" + mainServicePort + "/doctors/" + id;
            DoctorDTO doctor = restTemplate.getForObject(url, DoctorDTO.class);

            // Сохраняем в кеш и возвращаем
            DoctorCache.put(doctor);
            return doctor;
        });
    }
}
