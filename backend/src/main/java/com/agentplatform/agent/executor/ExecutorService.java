package com.agentplatform.agent.executor;


import org.springframework.stereotype.Service;

import com.agentplatform.agent.planner.PlanStep;
import com.agentplatform.agent.tool.Tool;
import com.agentplatform.agent.tool.ToolRegistry;
import com.agentplatform.llm.LlmService;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ExecutorService {

    private final ToolRegistry toolRegistry;
    private final LlmService llmService;

    public ExecutorService(ToolRegistry toolRegistry, LlmService llmService) {
        this.toolRegistry = toolRegistry;
        this.llmService = llmService;
    }

    // STREAMING EXECUTION (🔥 MAIN)
    public void executePlanStreaming(List<PlanStep> steps, String context, Consumer<String> consumer) {

        String lastResult = "";

        for (PlanStep step : steps) {

            // TOOL
            if ("tool".equals(step.getType())) {
                try {
                    Tool tool = toolRegistry.getTool(step.getTool());

                    if (tool != null) {
                        lastResult = tool.execute(step.getInput());
                        consumer.accept("\n[Tool Result]: " + lastResult + "\n");
                    } else {
                        consumer.accept("\n[Error]: Tool not found\n");
                    }

                } catch (Exception e) {
                    consumer.accept("\n[Error]: Tool failed\n");
                }
            }

            // LLM
            if ("llm".equals(step.getType())) {

                String prompt =
                        "Context:\n" + context + "\n\n" +
                        "Task:\n" + step.getInput() + "\n\n" +
                        "Previous result:\n" + lastResult;

                llmService.stream(prompt, consumer);
            }
        }
    }
}