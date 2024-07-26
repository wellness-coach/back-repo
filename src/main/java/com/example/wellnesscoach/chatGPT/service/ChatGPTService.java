package com.example.wellnesscoach.chatGPT.service;

import com.example.wellnesscoach.chatGPT.config.ChatGPTConfig;
import com.example.wellnesscoach.chatGPT.dto.ChatGPTRequestDTO;
import com.example.wellnesscoach.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.chatGPT.dto.QuestionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatGPTService {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ChatGPTConfig chatGPTConfig;

    @Autowired
    public ChatGPTService(RestTemplate restTemplate, HttpHeaders httpHeaders, ChatGPTConfig chatGPTConfig) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.chatGPTConfig = chatGPTConfig;
    }

    public HttpEntity<ChatGPTRequestDTO> buildHttpEntity(ChatGPTRequestDTO requestDto) {
        httpHeaders.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        httpHeaders.setBearerAuth(chatGPTConfig.API_KEY);
        return new HttpEntity<>(requestDto, httpHeaders);
    }

    public ChatGPTResponseDTO getResponse(HttpEntity<ChatGPTRequestDTO> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGPTResponseDTO> responseEntity = restTemplate.postForEntity(
                chatGPTConfig.getApiUrl(),
                chatGptRequestDtoHttpEntity,
                ChatGPTResponseDTO.class);

        return responseEntity.getBody();
    }

    public ChatGPTResponseDTO askQuestion(QuestionRequestDTO requestDto) {
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGPTRequestDTO(
                                ChatGPTConfig.MODEL,
                                List.of(new ChatGPTRequestDTO.Message("user", requestDto.getQuestion())),
                                ChatGPTConfig.MAX_TOKEN,
                                ChatGPTConfig.TEMPERATURE,
                                ChatGPTConfig.TOP_P
                        )
                )
        );
    }
}