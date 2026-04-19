package com.agentplatform.agent.planner;

import com.agentplatform.agent.tool.Tool;
import com.agentplatform.agent.tool.ToolRegistry;
import com.agentplatform.llm.LlmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    private static final Logger log = LoggerFactory.getLogger(PlannerService.class);

    private final LlmService llmService;
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlannerService(LlmService llmService, ToolRegistry toolRegistry) {
        this.llmService = llmService;
        this.toolRegistry = toolRegistry;
    }

    public List<PlanStep> createPlan(String userInput, String context) {
        log.info("[Planner] Creating plan for input: {}", userInput);
        String toolsDescription = buildToolDescriptions();
        String prompt = buildPrompt(userInput, context, toolsDescription);
        String response = llmService.callLLM(prompt);
        log.info("[Planner] Raw LLM response: {}", response);
        try {
            AgentDecision decision = objectMapper.readValue(response, AgentDecision.class);
            return buildPlanFromDecision(decision);
        } catch (Exception e) {
            log.error("[Planner] Failed to parse LLM response. Falling back.", e);
            return fallbackPlan(userInput);
        }
    }

    // -------------------------
    // PRIVATE METHODS
    // -------------------------

    private String buildToolDescriptions() {
        return toolRegistry.getAllTools()
                .stream()
                .map(t -> "- " + t.getName() + ": " + t.getDescription())
                .collect(Collectors.joining("\n"));
    }

    private String buildPrompt(String userInput, String context, String toolsDescription) {

        return """ 
        You are an AI agent.
        You can:
        - Answer directly
        - Use tools when needed

        Available tools:
        %s

        Context:
        %s

        User input:
        %s

        Rules:
        - Use tools ONLY if needed
        - Extract correct tool input
        - Otherwise answer directly

        Response format (STRICT JSON ONLY):

        If tool:
        {
        "type": "tool",
        "tool": "tool_name",
        "input": "input"
        }

        If direct:
        {
        "type": "llm",
        "input": "final answer"
        }

        NO TEXT OUTSIDE JSON.
        """.formatted(toolsDescription, context, userInput);
    }

    private List<PlanStep> buildPlanFromDecision(AgentDecision decision) {

        List<PlanStep> steps = new ArrayList<>();

        if ("tool".equalsIgnoreCase(decision.getType())) {

            steps.add(new PlanStep(
                    StepType.TOOL,
                    decision.getTool(),
                    decision.getInput()
            ));
            steps.add(new PlanStep(
                    StepType.LLM,
                    null,
                    "Explain the tool result to the user clearly."
            ));

        } else {

            steps.add(new PlanStep(
                    StepType.LLM,
                    null,
                    decision.getInput()
            ));
        }

        return steps;
    }

    private List<PlanStep> fallbackPlan(String userInput) {
        return List.of(
                new PlanStep(StepType.LLM, null, userInput)
        );
    }
}