package com.agentplatform.controller;

import com.agentplatform.agent.supervisor.SupervisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private static final Logger log = LoggerFactory.getLogger(AgentController.class);

    private final SupervisorService supervisorService;

    // Thread pool for async streaming
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public AgentController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    // 🔥 MAIN CHAT ENDPOINT (SSE STREAMING)
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam String input,
        @RequestParam(defaultValue = "default-user") String userId) {

        log.info("[Controller] Incoming request: {}", input);
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        executor.execute(() -> {
            try {

                supervisorService.streamRequest(userId, input, chunk -> {
                    try {
                        emitter.send(SseEmitter.event().data(chunk));
                    } catch (IOException e) {
                        log.error("[Controller] SSE send failed", e);
                        emitter.completeWithError(e);
                    }
                });

                emitter.complete();

            } catch (Exception e) {
                log.error("[Controller] Request failed", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}