package ru.hpclab.hl.module1.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.service.cache.DoctorRedisCache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CacheStatisticsService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DoctorRedisCache doctorRedisCache;

    public CacheStatisticsService(DoctorRedisCache customerCache) {
        this.doctorRedisCache = customerCache;
    }

    // Печать каждые 10 секунд
    @Scheduled(fixedRate = 10000)
    public void printCacheStats() {
        int size = doctorRedisCache.size();
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[CACHE] " + timestamp + " — Doctor cache size: " + size);
    }
}
