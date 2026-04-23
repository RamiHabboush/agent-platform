package com.agentplatform.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    public MetricsConfig(MeterRegistry registry) {
        // You can customize global tags here later
        registry.config().commonTags("app", "ai-agent-platform");
    }
}