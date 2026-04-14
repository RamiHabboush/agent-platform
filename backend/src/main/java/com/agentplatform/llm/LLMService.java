package com.agentplatform.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final ChatClient chatClient;

    public String chat(String input) {
        return chatClient.prompt()
                .user(input)
                .call()
                .content();
    }
}