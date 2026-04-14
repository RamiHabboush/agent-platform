package com.agentplatform.user;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import lombok.Data;

@Entity
@Data
public class User {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
