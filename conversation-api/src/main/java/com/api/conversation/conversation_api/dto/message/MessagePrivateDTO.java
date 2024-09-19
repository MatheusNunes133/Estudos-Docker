package com.api.conversation.conversation_api.dto.message;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessagePrivateDTO(
        @NotBlank
        String message,

        @NotBlank
        String sender,

        @NotBlank
        String recipient

) {
}
