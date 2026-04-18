package com.agentplatform.memory;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memory")
public class MemoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    private LocalDateTime createdAt = LocalDateTime.now();

    public MemoryEntry() {}

    public MemoryEntry(String userId, String content, float[] embedding) {
        this.userId = userId;
        this.content = content;
        this.embedding = embedding;
    }

    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public float[] getEmbedding() { return embedding; }
}