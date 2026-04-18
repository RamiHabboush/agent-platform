package com.agentplatform.controller;

import com.agentplatform.agent.supervisor.SupervisorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin("*")
public class AgentController {

    private final SupervisorService supervisorService;

    public AgentController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @GetMapping("/chat/stream")
    public SseEmitter stream(@RequestParam String input) {

        String userId = "demo-user";

        SseEmitter emitter = new SseEmitter(30_000L);

        Executors.newFixedThreadPool(10).submit(() -> {
            try {

                supervisorService.streamRequest(userId, input, chunk -> {
                    try {
                        emitter.send(chunk);
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                });

                emitter.complete();

            } catch (Exception e) {
                try {
                    emitter.send("Error: something went wrong");
                } catch (Exception ignored) {}
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}