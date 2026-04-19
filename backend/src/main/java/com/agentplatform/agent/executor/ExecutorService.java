package com.agentplatform.agent.executor;

import com.agentplatform.agent.planner.PlanStep;
import com.agentplatform.agent.planner.StepType;
import com.agentplatform.agent.tool.Tool;
import com.agentplatform.agent.tool.ToolRegistry;
import com.agentplatform.llm.LlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ExecutorService {

    private static final Logger log = LoggerFactory.getLogger(ExecutorService.class);
    private final ToolRegistry toolRegistry;
    private final LlmService llmService;

    public ExecutorService(ToolRegistry toolRegistry, LlmService llmService) {
        this.toolRegistry = toolRegistry;
        this.llmService = llmService;
    }

    // STREAMING EXECUTION (🔥 MAIN)
    public void executePlanStreaming(List<PlanStep> steps, String context, Consumer<String> consumer) {
        log.info("[Executor] Executing {} steps", steps.size());
        String lastResult = "";
        for (int i = 0; i < steps.size(); i++) {
            PlanStep step = steps.get(i);
            log.info("[Executor] Step {} → {}", i + 1, step.getType());
            try {
                if (step.getType() == StepType.TOOL) {
                    lastResult = executeTool(step, consumer);
                }
                if (step.getType() == StepType.LLM) {
                    executeLLM(step, context, lastResult, consumer);
                }
            } catch (Exception e) {
                log.error("[Executor] Step failed", e);
                consumer.accept("\n[Error]: Step execution failed\n");
            }
        }
    }

    // TOOL EXECUTION
    private String executeTool(PlanStep step, Consumer<String> consumer) {
        String toolName = step.getTool();
        log.info("[Executor] Using tool: {}", toolName);
        Tool tool = toolRegistry.getTool(toolName);
        if (tool == null) {
            log.warn("[Executor] Tool not found: {}", toolName);
            consumer.accept("\n[Error]: Tool not found\n");
            return "";
        }
        try {
            String result = tool.execute(step.getInput());
            consumer.accept("\n[Tool Result]: " + result + "\n");
            return result;
        } catch (Exception e) {
            log.error("[Executor] Tool execution failed: {}", toolName, e);
            consumer.accept("\n[Error]: Tool failed\n");
            return "";
        }
    }

    // LLM EXECUTION (STREAMING)
    private void executeLLM(PlanStep step, String context, String lastResult, Consumer<String> consumer) {
        log.info("[Executor] Calling LLM");
        String prompt = buildPrompt(step.getInput(), context, lastResult);
        llmService.stream(prompt, consumer);
    }
    
    // PROMPT BUILDER
    private String buildPrompt(String task, String context, String lastResult) {

        return """
        Context:
        %s

        Previous result:
        %s

        Task:
        %s

        Instructions:
        - Answer clearly
        - Use previous result if relevant
        - Be concise but helpful
        """
        .formatted(context, lastResult, task);
    }
}