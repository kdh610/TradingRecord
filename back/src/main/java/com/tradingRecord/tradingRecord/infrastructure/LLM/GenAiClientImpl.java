package com.tradingRecord.tradingRecord.infrastructure.LLM;

import com.tradingRecord.tradingRecord.application.LLM.ChatModelClient;
import com.tradingRecord.tradingRecord.application.LLM.EmbeddingService;
import com.tradingRecord.tradingRecord.presentation.dto.AiCommentRequest;
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
    public String sendQuestion(AiCommentRequest request, String targetTradeInfo){

        Prompt prompt = makePrompt(request, targetTradeInfo);
        ChatResponse call = genAiChatModel.call(prompt);

        log.info("call {}", call.getResult().getOutput().getText());
        return call.getResult().getOutput().getText();
    }

    private Prompt makePrompt(AiCommentRequest request, String targetTradeInfo){

        String marketContext = embeddingService.searchMarketDocs(request)
                .stream().findFirst().map(Document::getText)
                .orElse("당일 시황 없음");

        String similarTrades = embeddingService.searchTradeDocs(request)
                .stream().map(Document::getText).collect(Collectors.joining("\n---\n"));

        String theoryContext = embeddingService.searchTheoryDocs(request)
                .stream().map(Document::getText).collect(Collectors.joining("\n---\n"));

        PromptTemplate promptTemplate = new PromptTemplate(coachPromptResource);
        promptTemplate.add("currentTrade", targetTradeInfo);
        promptTemplate.add("similarTrades", similarTrades);
        promptTemplate.add("marketContext", marketContext);
        promptTemplate.add("theoryContext", theoryContext);
        Prompt prompt = promptTemplate.create();
        log.info("prompt: {}", prompt);
        return prompt;
    }



}
