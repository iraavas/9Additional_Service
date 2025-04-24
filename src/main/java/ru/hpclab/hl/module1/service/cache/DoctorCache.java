package ru.hpclab.hl.module1.service.cache;

import ru.hpclab.hl.module1.dto.DoctorDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DoctorCache {

    private static final Map<Long, DoctorDTO> cache = new HashMap<>();

    public static Optional<DoctorDTO> get(Long id) {
        DoctorDTO result = cache.get(id);
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[CACHE] " + timestamp + " get(" + id + ") => " + (result == null ? "MISS" : "HIT"));
        return Optional.ofNullable(result);
    }

    public static void put(DoctorDTO doctor) {
        if (doctor != null && doctor.getId() != null) {
            cache.put(doctor.getId(), doctor);
        }
    }

    public static boolean contains(Long id) {
        return cache.containsKey(id);
    }

    public static int size() {
        return cache.size();
    }

    public static void clear() {
        cache.clear();
    }
}
