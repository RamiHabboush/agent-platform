package com.agentplatform.agent.planner;

public class PlanStep {

    private StepType type;
    private String tool;
    private String input;

    public PlanStep() {}

    public PlanStep(StepType type, String tool, String input) {
        this.type = type;
        this.tool = tool;
        this.input = input;
    }
    // Getters and setters
    public StepType getType() {return type;}
    public String getTool() {return tool;}
    public String getInput() {return input;}
    public void setType(StepType type) { this.type = type;}
    public void setTool(String tool) {this.tool = tool;}
    public void setInput(String input) {this.input = input;}
}