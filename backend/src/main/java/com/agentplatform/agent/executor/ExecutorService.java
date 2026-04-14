package com.agentplatform.agent.executor;

import org.springframework.stereotype.Service;
import com.agentplatform.agent.tool.WeatherTool;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExecutorService {

    private final WeatherTool weatherTool;

    public String execute(String input) {
        if (input.toLowerCase().contains("weather")) {
            return weatherTool.getWeather("Berlin");
        }
        return "No tool used.";
    }
}
