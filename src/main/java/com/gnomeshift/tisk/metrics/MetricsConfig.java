package com.gnomeshift.tisk.metrics;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public MeterFilter heapOnlyFilter() {
        return MeterFilter.deny(id ->
                "jvm.memory.used".equals(id.getName()) && !"heap".equals(id.getTag("area"))
        );
    }

    @Bean
    public MeterFilter errorLogOnlyFilter() {
        return MeterFilter.deny(id ->
                "logback.events".equals(id.getName()) && !"error".equals(id.getTag("level"))
        );
    }
}
