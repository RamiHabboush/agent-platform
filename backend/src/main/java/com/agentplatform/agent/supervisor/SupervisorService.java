package com.agentplatform.agent.supervisor;

import com.agentplatform.agent.executor.ExecutorService;
import com.agentplatform.agent.planner.PlanStep;
import com.agentplatform.agent.planner.PlannerService;
import com.agentplatform.memory.MemoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class SupervisorService {

    private final PlannerService plannerService;
    private final ExecutorService executorService;
    private final MemoryService memoryService;

    public SupervisorService(PlannerService plannerService,
                             ExecutorService executorService,
                             MemoryService memoryService) {
        this.plannerService = plannerService;
        this.executorService = executorService;
        this.memoryService = memoryService;
    }

    public void streamRequest(String userId, String input, Consumer<String> consumer) {

        // Save user message
        memoryService.saveMessage(userId, "User: " + input);
        // Get memory context
        List<String> memories = memoryService.searchMemory(userId, input);
        String context = String.join("\n", memories);
        // Plan
        plannerService.createPlan(input, context);
        List<PlanStep> plan = plannerService.createPlan(input, context);
        // Execute streaming
        executorService.executePlanStreaming(plan, context, consumer);
    }
}