package com.agentplatform.agent.tool.impl;

import com.agentplatform.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool implements Tool {

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getDescription() {
        return "Get current weather for a city. Input should be a city name like 'Berlin'.";
    }

    @Override
    public String execute(String input) {

        if (input == null || input.isBlank()) {
            return "Invalid input for weather tool.";
        }

        // MOCK (replace later with real API)
        return "Weather in " + input + " is sunny, 20°C.";
    }
}