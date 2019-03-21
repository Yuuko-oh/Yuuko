package com.yuuko.core.metrics;

import java.lang.management.ManagementFactory;

public class SystemMetrics {
    public long UPTIME = 0;
    public long MEMORY_TOTAL = 0;
    public long MEMORY_USED = 0;

    /**
     * Updates all of the system metrics
     */
    public void update() {
        UPTIME = ManagementFactory.getRuntimeMXBean().getUptime();
        MEMORY_TOTAL = Runtime.getRuntime().totalMemory();
        MEMORY_USED = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }
}
