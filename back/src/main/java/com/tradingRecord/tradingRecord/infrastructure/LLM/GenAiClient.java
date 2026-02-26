package com.tradingRecord.tradingRecord.infrastructure.LLM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenAiClient {


    private final GoogleGenAiChatModel genAiChatModel;

    public void sendQuestion(){
        ChatResponse call = genAiChatModel.call(
                new Prompt("Generate the names of 5 famous pirates.",
                        GoogleGenAiChatOptions.builder()
                                .temperature(0.4)
                                .build())
        );
        log.info("call {}", call);

    }



}
