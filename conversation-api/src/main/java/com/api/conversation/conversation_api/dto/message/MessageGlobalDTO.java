package com.api.conversation.conversation_api.dto.message;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessageGlobalDTO(
        @NotBlank
        String message,

        @NotBlank
        UUID sender

) {
}
