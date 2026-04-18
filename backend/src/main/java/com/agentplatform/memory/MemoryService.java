package com.agentplatform.memory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryService {

    private final MemoryRepository repository;
    private final EmbeddingService embeddingService;

    public MemoryService(MemoryRepository repository,
                          EmbeddingService embeddingService) {
        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    // SAVE MEMORY
    public void saveMessage(String userId, String message) {

        float[] embedding = embeddingService.embed(message);

        MemoryEntry entry = new MemoryEntry(userId, message, embedding);

        repository.save(entry);
    }

    // SIMPLE RETRIEVAL (we improve later with vector search)
    public List<String> getRecentMessages(String userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(MemoryEntry::getContent)
                .collect(Collectors.toList());
    }

    private double cosine(float[] a, float[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public List<String> searchMemory(String userId, String query) {
        float[] queryEmbedding = embeddingService.embed(query);
        return repository.findByUserId(userId)
                .stream()
                .sorted((a, b) -> Double.compare(
                        cosine(queryEmbedding, b.getEmbedding()),
                        cosine(queryEmbedding, a.getEmbedding())
                ))
                .limit(5)
                .map(MemoryEntry::getContent)
                .collect(Collectors.toList());
    }
}