package com.agentplatform.agent.tool;

import org.springframework.stereotype.Component;

@Component
public class WeatherTool implements Tool {

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getDescription() {
        return "Get weather information for a given city. Input should be a city name.";
    }

    @Override
    public String execute(String input) {
        return "Weather for " + input + " is SUNNY (mock data)";
    }
}
