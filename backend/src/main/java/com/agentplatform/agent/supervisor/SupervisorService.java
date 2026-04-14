package com.agentplatform.agent.supervisor;

import org.springframework.stereotype.Service;
import com.agentplatform.agent.executor.ExecutorService;
import com.agentplatform.llm.LLMService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupervisorService {

    private final LLMService llm;
    private final ExecutorService executor;

    public String route(String input) {
        if (input.contains("weather")) {
            return executor.execute(input);
        }
        return llm.chat(input);
    }
}