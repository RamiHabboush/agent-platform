package com.agentplatform.agent.tool;

public interface Tool {
    // Unique name used by LLM
    String getName();
    // Description used by planner (VERY IMPORTANT)
    String getDescription();
    // Execute tool logic
    String execute(String input);
}
