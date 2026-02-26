package com.tradingRecord.tradingRecord.infrastructure.LLM;

import com.tradingRecord.tradingRecord.application.LLM.ChatModelClient;
import com.tradingRecord.tradingRecord.application.LLM.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenAiClientImpl implements ChatModelClient {

    @Value("classpath:prompts/prompt.st")
    private Resource coachPromptResource;

    private final GoogleGenAiChatModel genAiChatModel;
    private final EmbeddingService embeddingService;

    @Override
    public String sendQuestion(String query){

        Prompt prompt = makePrompt(query);
        ChatResponse call = genAiChatModel.call(prompt);

        log.info("call {}", call.getResult().getOutput().getText());
        return call.getResult().getOutput().getText();
    }

    public Prompt makePrompt(String query){
        List<Document> documents = embeddingService.similaritySearch(query);
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        PromptTemplate promptTemplate = new PromptTemplate(coachPromptResource);
        promptTemplate.add("tradeData", context);
        Prompt prompt = promptTemplate.create();
        log.info("prompt: {}", prompt);
        return prompt;
    }



}
