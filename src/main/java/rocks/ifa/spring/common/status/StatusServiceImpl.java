package rocks.ifa.spring.common.status;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.TimeGauge;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final MeterRegistry meterRegistry;
    private Duration startupTime;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        this.startupTime = event.getTimeTaken();
    }

    @Override
    public Map<String, Object> getServerStatus() {
        Map<String, Object> status = new HashMap<>();

        // 1. Startup Time
        if (startupTime != null) {
            status.put("startup_time_seconds", String.format("%.3f", startupTime.toMillis() / 1000.0));
        }

        // 2. Uptime
        findTimeGauge("process.uptime").ifPresent(uptimeGauge -> {
            long uptimeSeconds = (long) uptimeGauge.value(TimeUnit.SECONDS);
            status.put("uptime_seconds", uptimeSeconds);
            status.put("uptime_formatted", formatUptime(uptimeSeconds));
        });

        // 3. JVM Memory Usage
        findGauge("jvm.memory.used").ifPresent(used -> {
            long memoryUsed = (long) used.value(); // Corrected
            status.put("memory_used_mb", toMB(memoryUsed));
            findGauge("jvm.memory.max").ifPresent(max -> {
                long memoryMax = (long) max.value(); // Corrected
                status.put("memory_max_mb", toMB(memoryMax));
                if (memoryMax > 0) {
                    status.put("memory_usage_percentage", String.format("%.2f%%", (double) memoryUsed / memoryMax * 100));
                }
            });
        });

        // 4. CPU Usage
        findGauge("system.cpu.usage").ifPresent(cpuUsage ->
            status.put("cpu_usage_percentage", String.format("%.2f%%", cpuUsage.value() * 100))
        );

        // 5. Live Threads
        findGauge("jvm.threads.live").ifPresent(threadCount ->
            status.put("live_threads", (long) threadCount.value()) // Corrected
        );

        return status;
    }

    private Optional<Gauge> findGauge(String name) {
        return Optional.ofNullable(meterRegistry.find(name).gauge());
    }

    private Optional<TimeGauge> findTimeGauge(String name) {
        return Optional.ofNullable(meterRegistry.find(name).timeGauge());
    }

    private long toMB(long bytes) {
        return bytes / (1024 * 1024);
    }

    private String formatUptime(long totalSeconds) {
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = totalSeconds / 3600 % 24;
        long minutes = totalSeconds / 60 % 60;
        long seconds = totalSeconds % 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}
