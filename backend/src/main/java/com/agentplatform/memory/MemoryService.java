package com.agentplatform.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.agentplatform.memory.MemoryRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryService {

    private static final Logger log = LoggerFactory.getLogger(MemoryService.class);

    private final MemoryRepository memoryRepository;
    private final EmbeddingService embeddingService;

    public MemoryService(MemoryRepository memoryRepository,
                         EmbeddingService embeddingService) {
        this.memoryRepository = memoryRepository;
        this.embeddingService = embeddingService;
    }

    // SAVE MESSAGE
    public void saveMessage(String userId, String message) {

        try {
            float[] embedding = embeddingService.embed(message);

            Memory memory = new Memory(userId, message, embedding);

            memoryRepository.save(memory);

            log.info("[Memory] Saved message for user {}", userId);

        } catch (Exception e) {
            log.error("[Memory] Save failed", e);
        }
    }

    // SEARCH (RAG)
    public List<String> searchMemory(String userId, String query) {

        try {
            float[] queryEmbedding = embeddingService.embed(query);

            List<Memory> results =
                    memoryRepository.searchSimilar(userId, queryEmbedding);

            return results.stream()
                    .map(Memory::getContent)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("[Memory] Search failed", e);
            return List.of();
        }
    }
}