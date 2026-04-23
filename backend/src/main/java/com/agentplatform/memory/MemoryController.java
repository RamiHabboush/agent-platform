package com.agentplatform.memory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memory")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping("/save")
    public String save(@RequestParam String userId,
                       @RequestParam String message) {

        memoryService.saveMessage(userId, message);
        return "Saved";
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam String userId,
                               @RequestParam String query) {

        return memoryService.searchMemory(userId, query);
    }
}