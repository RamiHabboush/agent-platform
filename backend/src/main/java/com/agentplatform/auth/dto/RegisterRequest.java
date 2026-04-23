package com.agentplatform.auth.dto;

public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    public RegisterRequest() {}

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}