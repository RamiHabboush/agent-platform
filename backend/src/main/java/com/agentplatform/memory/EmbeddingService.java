package com.agentplatform.memory;

import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private final OpenAiClient openAiClient;

    public EmbeddingService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public float[] embed(String text) {

        // pseudo-code depending on your OpenAI setup
        return openAiClient.embeddings()
                .create(text)
                .getEmbedding();
    }
}