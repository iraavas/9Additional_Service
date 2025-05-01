package ru.hpclab.hl.module1.sсheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.hpclab.hl.module1.client.CrashClient;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final CrashClient crashClient;

    @Scheduled(fixedDelayString = "${CORE_CRASH_DELAY:10000}")
    public void callCrashEndpoint() {
        try {
            crashClient.crashCore();
            System.out.println("Crash request sent to Core Service");
        } catch (Exception e) {
            System.err.println("Failed to call crash endpoint: " + e.getMessage());
        }
    }
}
