package com.agentplatform.agent.planner;

public class PlanStep {

    private String type; // "tool" or "llm"
    private String tool; // e.g. "weather"
    private String input;

    public PlanStep() {}

    public PlanStep(String type, String tool, String input) {
        this.type = type;
        this.tool = tool;
        this.input = input;
    }

    public String getType() { return type; }
    public String getTool() { return tool; }
    public String getInput() { return input; }

    public void setType(String type) { this.type = type; }
    public void setTool(String tool) { this.tool = tool; }
    public void setInput(String input) { this.input = input; }
}