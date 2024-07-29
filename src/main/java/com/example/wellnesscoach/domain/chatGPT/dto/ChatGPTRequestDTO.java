package com.example.wellnesscoach.domain.chatGPT.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

//ChatGptRequestDto
@Getter
@NoArgsConstructor
public class ChatGPTRequestDTO implements Serializable {

    private String model;
    private List<Message> messages;
    private Integer max_tokens;
    private Double temperature;
    private Double top_p;

    @Builder
    public ChatGPTRequestDTO(String model, List<Message> messages, Integer max_tokens, Double temperature, Double top_p) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
        this.temperature = temperature;
        this.top_p = top_p;
    }

    @Getter
    @NoArgsConstructor
    public static class Message implements Serializable {
        private String role;
        private String content;

        @Builder
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}