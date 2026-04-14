package com.agentplatform.memory;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemoryService {

    private final List<String> memory = new ArrayList<>();

    public void save(String text) {
        memory.add(text);
    }

    public List<String> search(String query) {
        return memory.stream()
                .filter(m -> m.contains(query))
                .toList();
    }
}
