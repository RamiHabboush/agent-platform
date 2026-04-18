package com.agentplatform.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;

@Service
public class LlmService {

    private final ChatClient chatClient;

    public LlmService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // NORMAL CALL
    public String callLLM(String prompt) {
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    // STREAMING CALL (🔥 IMPORTANT)
    public void stream(String prompt, Consumer<String> consumer) {
        chatClient
                .prompt()
                .user(prompt)
                .stream()
                .content()
                .subscribe(consumer);
    }
}