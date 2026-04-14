package com.agentplatform.agent.executor;

import org.springframework.stereotype.Service;
import com.agentplatform.agent.tool.WeatherTool;

@Service
public class ExecutorService {

    private final WeatherTool weatherTool;

    public ExecutorService(WeatherTool weatherTool) {
        this.weatherTool = weatherTool;
    }

    public String execute(String input) {
        if (input.toLowerCase().contains("weather")) {
            return weatherTool.getWeather("Berlin");
        }
        return "No tool used.";
    }
}
