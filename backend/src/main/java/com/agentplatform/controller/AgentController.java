package com.agentplatform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agentplatform.agent.supervisor.SupervisorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AgentController {

    private final SupervisorService supervisor;

    @GetMapping("/agent")
    public String run(@RequestParam String input) {
        return supervisor.route(input);
    }
}
