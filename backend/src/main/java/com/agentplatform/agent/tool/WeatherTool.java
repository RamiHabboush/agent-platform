package com.agentplatform.agent.tool;

import org.springframework.stereotype.Component;

@Component
public class WeatherTool {

    public String getWeather(String city) {
        return "Weather in " + city + ": 20°C, Sunny";
    }
}
