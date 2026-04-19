package com.agentplatform.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingConfig implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
        throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        try {
            System.out.println("[REQ " + requestId + "] " +
                    req.getMethod() + " " + req.getRequestURI());
            chain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("[REQ " + requestId + "] ERROR: " + e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }
    }
}