package com.en_chu.calculator_api_spring.service;

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
public class StatusService {

    private final MeterRegistry meterRegistry;

    public Map<String, Object> getServerStatus() {
        Map<String, Object> status = new HashMap<>();

        // 1. JVM 記憶體使用情況 (安全獲取)
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

        // 2. CPU 使用率 (安全獲取)
        findGaugeValue("system.cpu.usage").ifPresent(cpuUsage ->
            status.put("cpu_usage_percentage", String.format("%.2f%%", cpuUsage * 100))
        );

        // 3. 應用程式運行時間 (安全獲取)
        Optional.ofNullable(meterRegistry.find("process.uptime").timeGauge())
            .ifPresent(uptimeGauge -> {
                long uptimeSeconds = (long) uptimeGauge.value(TimeUnit.SECONDS);
                status.put("uptime_seconds", uptimeSeconds);
                status.put("uptime_formatted", formatUptime(uptimeSeconds));
            });

        // 4. 當前線程數 (安全獲取)
        findGaugeValue("jvm.threads.live").ifPresent(threadCount ->
            status.put("live_threads", threadCount.longValue())
        );

        return status;
    }

    /**
     * 安全地獲取一個 Gauge 指標的值。
     * @param name 指標名稱
     * @return 一個包含 Double 值的 Optional，如果指標不存在則為 empty。
     */
    private Optional<Double> findGaugeValue(String name) {
        return Optional.ofNullable(meterRegistry.find(name).gauge())
                       .map(Gauge::value);
    }

    private long toMB(long bytes) {
        return bytes / (1024 * 1024);
    }

    private String formatUptime(long totalSeconds) {
        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }
}
