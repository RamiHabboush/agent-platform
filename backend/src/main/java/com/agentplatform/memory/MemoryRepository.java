package com.agentplatform.memory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoryRepository extends JpaRepository<MemoryEntry, Long> {

    @Query(value = """
        SELECT * FROM memory
        WHERE user_id = :userId
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<MemoryEntry> findSimilar(
            @Param("userId") String userId,
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}