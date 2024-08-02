package com.api.conversation.conversation_api.dto.singup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String name,

        @NotBlank
        String password,

        @NotBlank
        String nickname
) {
}
