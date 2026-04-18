package com.agentplatform.agent.planner;

public class AgentDecision {

    private String type; // "tool" or "llm"
    private String tool;
    private String input;

    public String getType() { return type; }
    public String getTool() { return tool; }
    public String getInput() { return input; }

    public void setType(String type) { this.type = type; }
    public void setTool(String tool) { this.tool = tool; }
    public void setInput(String input) { this.input = input; }
}