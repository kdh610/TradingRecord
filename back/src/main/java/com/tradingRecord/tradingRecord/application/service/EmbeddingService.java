package com.tradingRecord.tradingRecord.application.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface EmbeddingService {
    void saveEmbeddingInfo(Document document);
    List<Document> similaritySearch(String query);
}
