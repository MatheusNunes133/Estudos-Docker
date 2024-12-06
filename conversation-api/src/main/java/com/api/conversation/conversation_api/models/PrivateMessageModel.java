package com.api.conversation.conversation_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb-message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime = LocalDateTime.now();

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserModel recipient;

}
