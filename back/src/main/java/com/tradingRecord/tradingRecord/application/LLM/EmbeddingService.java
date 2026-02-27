package com.tradingRecord.tradingRecord.application.LLM;

import com.tradingRecord.tradingRecord.presentation.dto.AiCommentRequest;
import org.springframework.ai.document.Document;

import java.util.List;

public interface EmbeddingService {
    void saveEmbeddingInfo(Document document);
    List<Document> searchTradeDocs(AiCommentRequest request);
    List<Document> searchTheoryDocs(AiCommentRequest request);
    List<Document> searchMarketDocs(AiCommentRequest request);
}
