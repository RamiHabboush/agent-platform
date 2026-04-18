package com.agentplatform.agent.planner;

import com.agentplatform.agent.tool.Tool;
import com.agentplatform.agent.tool.ToolRegistry;
import com.agentplatform.llm.LlmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    private final LlmService llmService;
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlannerService(LlmService llmService, ToolRegistry toolRegistry) {
        this.llmService = llmService;
        this.toolRegistry = toolRegistry;
    }

    public List<PlanStep> createPlan(String userInput, String context) {

        // 🔥 Build dynamic tool list
        String toolsDescription = toolRegistry.getAllTools()
                .stream()
                .map(tool -> "- " + tool.getName() + ": " + tool.getDescription())
                .collect(Collectors.joining("\n"));

        // 🔥 Strong prompt
        String prompt = """
You are an AI agent.

You can:
- Answer questions directly
- Use tools when needed

Available tools:
%s

Context (previous conversation):
%s

User input:
%s

Instructions:
- Decide the BEST next action
- Use a tool ONLY if necessary
- If using a tool, extract the correct input

Response format (STRICT JSON ONLY):

If using tool:
{
  "type": "tool",
  "tool": "tool_name",
  "input": "input_for_tool"
}

If answering directly:
{
  "type": "llm",
  "input": "final answer or refined question"
}

DO NOT return anything except JSON.
""".formatted(toolsDescription, context, userInput);

        String response = llmService.callLLM(prompt);

        try {
            AgentDecision decision = objectMapper.readValue(response, AgentDecision.class);

            List<PlanStep> steps = new ArrayList<>();

            if ("tool".equalsIgnoreCase(decision.getType())) {

                steps.add(new PlanStep(
                        "tool",
                        decision.getTool(),
                        decision.getInput()
                ));

                steps.add(new PlanStep(
                        "llm",
                        null,
                        "Explain the result to the user clearly."
                ));

            } else {

                steps.add(new PlanStep(
                        "llm",
                        null,
                        decision.getInput()
                ));
            }

        return steps;
        } catch (Exception e) {
            return List.of( new PlanStep("llm", null, userInput));
        }
    }
}