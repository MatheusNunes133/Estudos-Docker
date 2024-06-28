package com.api.conversation.conversation_api.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb-user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
