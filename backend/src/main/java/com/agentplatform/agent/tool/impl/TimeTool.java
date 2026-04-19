package com.agentplatform.agent.tool.impl;

import org.springframework.stereotype.Component;

import com.agentplatform.agent.tool.Tool;

@Component
public class TimeTool implements Tool {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getDescription() {
        return "Get current time.";
    }

    @Override
    public String execute(String input) {
        return java.time.LocalTime.now().toString();
    }
}