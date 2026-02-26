package com.tradingRecord.tradingRecord.infrastructure.LLM;

import com.tradingRecord.tradingRecord.application.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {
    private final VectorStore vectorStore;


    @Override
    public void saveEmbeddingInfo(Document document) {
        List<Document> documents = List.of(document);
        vectorStore.add(documents);
    }

    @Override
    public List<Document> similaritySearch(String query) {
        return this.vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(5).build());
    }
}
