package ru.hpclab.hl.module1.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.service.cache.DoctorCache;

@Service
public class CacheStatisticsService {

    // Печать каждые 10 секунд (можно изменить на нужный интервал)
    @Scheduled(fixedRate = 10000)
    public void printCacheStats() {
        int size = DoctorCache.size();
        System.out.println("[CACHE] Doctor cache size: " + size);
    }
}
