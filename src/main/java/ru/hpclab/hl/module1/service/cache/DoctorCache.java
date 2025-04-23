package ru.hpclab.hl.module1.service.cache;

import ru.hpclab.hl.module1.dto.DoctorDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DoctorCache {

    private static final Map<Long, DoctorDTO> cache = new HashMap<>();

    public static Optional<DoctorDTO> get(Long id) {
        return Optional.ofNullable(cache.get(id));
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
