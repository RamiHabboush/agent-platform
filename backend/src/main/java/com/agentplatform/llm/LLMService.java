package com.agentplatform.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LLMService {

    private final ChatClient chatClient;

    public LLMService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String input) {
        return chatClient.prompt()
                .user(input)
                .call()
                .content();
    }
}