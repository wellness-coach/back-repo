package com.example.wellnesscoach.chatGPT.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

// ChatGptResponseDto
@Getter
@NoArgsConstructor
public class ChatGPTResponseDTO implements Serializable {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    @Builder
    public ChatGPTResponseDTO(String id, String object, long created, String model, List<Choice> choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }

    @Getter
    @NoArgsConstructor
    public static class Choice implements Serializable {
        private Message message;
        private Integer index;
        private String finish_reason;

        @Builder
        public Choice(Message message, Integer index, String finish_reason) {
            this.message = message;
            this.index = index;
            this.finish_reason = finish_reason;
        }
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