package com.agentplatform.agent.supervisor;

import com.agentplatform.agent.executor.ExecutorService;
import com.agentplatform.agent.planner.PlanStep;
import com.agentplatform.agent.planner.PlannerService;
import com.agentplatform.memory.MemoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class SupervisorService {

    private static final Logger log = LoggerFactory.getLogger(SupervisorService.class);

    private final PlannerService plannerService;
    private final ExecutorService executorService;
    private final MemoryService memoryService;

    public SupervisorService(PlannerService plannerService, ExecutorService executorService, MemoryService memoryService) {
        this.plannerService = plannerService;
        this.executorService = executorService;
        this.memoryService = memoryService;
    }

    // 🔥 MAIN ENTRY POINT (STREAMING)
    public void streamRequest(String userId, String input, Consumer<String> consumer) {

        String requestId = UUID.randomUUID().toString();
        log.info("[{}][Supervisor] New request: {}", requestId, input);
        try {
            // 1. SAVE USER MESSAGE
            memoryService.saveMessage(userId, "User: " + input);
            // 2. RETRIEVE CONTEXT (SEMANTIC)
            List<String> memories = memoryService.searchMemory(userId, input);
            String context = String.join("\n", memories);
            log.info("[{}][Supervisor] Retrieved {} memory items", requestId, memories.size());
            // 3. CREATE PLAN
            List<PlanStep> plan = plannerService.createPlan(input, context);
            log.info("[{}][Supervisor] Plan created with {} steps", requestId, plan.size());
            // 4. EXECUTE PLAN (STREAM)
            StringBuilder fullResponse = new StringBuilder();
            executorService.executePlanStreaming(
                    plan,
                    context,
                    chunk -> {
                        try {
                            consumer.accept(chunk);
                            fullResponse.append(chunk);
                        } catch (Exception e) {
                            log.error("[{}][Supervisor] Streaming error", requestId, e);
                        }
                    }
            );
            // 5. SAVE AI RESPONSE
            memoryService.saveMessage(userId, "AI: " + fullResponse);
            log.info("[{}][Supervisor] Request completed", requestId);
        } catch (Exception e) {
            log.error("[{}][Supervisor] Request failed", requestId, e);
            consumer.accept("\n[Error]: Something went wrong\n");
        }
    }
}