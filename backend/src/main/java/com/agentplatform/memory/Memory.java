package com.agentplatform.memory;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memory")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Memory() {}

    public Memory(String userId, String content, float[] embedding) {
        this.userId = userId;
        this.content = content;
        this.embedding = embedding;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}