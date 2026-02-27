package com.tradingRecord.tradingRecord.application.LLM;

import com.tradingRecord.tradingRecord.presentation.dto.AiCommentRequest;

public interface ChatModelClient {
    String sendQuestion(AiCommentRequest request ,String content);
}
