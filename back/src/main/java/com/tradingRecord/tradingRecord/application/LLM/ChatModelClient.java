package com.tradingRecord.tradingRecord.application.LLM;

import com.tradingRecord.tradingRecord.presentation.dto.AiCommentRequest;

import java.time.LocalDate;

public interface ChatModelClient {
    String requestTradeReview(AiCommentRequest request ,String content);
    String requestOverallReview(LocalDate date);
}
