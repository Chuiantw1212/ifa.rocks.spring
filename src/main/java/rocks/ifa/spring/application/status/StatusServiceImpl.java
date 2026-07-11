package rocks.ifa.spring.application.status;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final MeterRegistry meterRegistry;

    @Override
    public Map<String, Object> getServerStatus() {
        Map<String, Object> status = new HashMap<>();

        // 1. JVM Memory Usage
        findGaugeValue("jvm.memory.used").ifPresent(used -> {
            long memoryUsed = used.longValue();
            status.put("memory_used_mb", toMB(memoryUsed));

            findGaugeValue("jvm.memory.max").ifPresent(max -> {
                long memoryMax = max.longValue();
                status.put("memory_max_mb", toMB(memoryMax));
                if (memoryMax > 0) {
                    status.put("memory_usage_percentage", String.format("%.2f%%", (double) memoryUsed / memoryMax * 100));
                }
            });
        });

        // 2. CPU Usage
        findGaugeValue("system.cpu.usage").ifPresent(cpuUsage ->
            status.put("cpu_usage_percentage", String.format("%.2f%%", cpuUsage * 100))
        );

        // 3. Uptime
        Optional.ofNullable(meterRegistry.find("process.uptime").timeGauge())
            .ifPresent(uptimeGauge -> {
                long uptimeSeconds = (long) uptimeGauge.value(TimeUnit.SECONDS);
                status.put("uptime_seconds", uptimeSeconds);
                status.put("uptime_formatted", formatUptime(uptimeSeconds));
            });

        // 4. Live Threads
        findGaugeValue("jvm.threads.live").ifPresent(threadCount ->
            status.put("live_threads", threadCount.longValue())
        );

        return status;
    }

    private Optional<Double> findGaugeValue(String name) {
        return Optional.ofNullable(meterRegistry.find(name).gauge())
                       .map(Gauge::value);
    }

    private long toMB(long bytes) {
        return bytes / (1024 * 1024);
    }

    private String formatUptime(long totalSeconds) {
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = TimeUnit.SECONDS.toHours(totalSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        long seconds = totalSeconds % 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}
