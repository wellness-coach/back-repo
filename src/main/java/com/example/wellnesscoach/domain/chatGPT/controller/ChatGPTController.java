package com.example.wellnesscoach.domain.chatGPT.controller;

import com.example.wellnesscoach.domain.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.domain.chatGPT.dto.QuestionRequestDTO;
import com.example.wellnesscoach.domain.chatGPT.service.ChatGPTService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-gpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/question")
    public ChatGPTResponseDTO sendQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        return chatGPTService.askQuestion(questionRequestDTO);
    }
}