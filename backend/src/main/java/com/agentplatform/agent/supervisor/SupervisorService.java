package com.agentplatform.agent.supervisor;

import org.springframework.stereotype.Service;
import com.agentplatform.agent.executor.ExecutorService;
import com.agentplatform.llm.LLMService;

@Service
public class SupervisorService {

    private final LLMService llm;
    private final ExecutorService executor;

    public SupervisorService(LLMService llm, ExecutorService executor) {
        this.llm = llm;
        this.executor = executor;
    }

    public String route(String input) {
        if (input.contains("weather")) {
            return executor.execute(input);
        }
        return llm.chat(input);
    }
}