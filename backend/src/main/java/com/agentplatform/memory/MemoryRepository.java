package com.agentplatform.memory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    List<Memory> findByUserId(String userId);

    @Query(value = """
        SELECT * FROM memory
        WHERE user_id = :userId
        ORDER BY embedding <-> : embedding
        LIMIT 5
        """, nativeQuery = true)
    List<Memory> searchSimilar(
            @Param("userId") String userId,
            @Param("embedding") float[] embedding
    );
}