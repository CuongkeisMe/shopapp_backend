package com.example.shopapp_backend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder

public class RegisterResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("token")
    private String token;
}
