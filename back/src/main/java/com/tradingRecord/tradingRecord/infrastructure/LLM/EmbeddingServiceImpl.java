package com.tradingRecord.tradingRecord.infrastructure.LLM;

import com.tradingRecord.tradingRecord.application.LLM.EmbeddingService;
import com.tradingRecord.tradingRecord.presentation.dto.AiCommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    public List<Document> searchTradeDocs(AiCommentRequest request) {
        String tradeQuery = buildTradeSearchQuery(request);

        FilterExpressionBuilder b = new FilterExpressionBuilder();
        FilterExpressionBuilder.Op base = b.eq("type", "trade");

        if(request.id()!=null){
            b.and(base, b.eq("id", request.id().toString()));
        }

        if(request.stupid()!=null && request.stupid()){
            b.and(base, b.eq("stupid", true));
        }else{
            b.and(base, b.eq("stupid", false));
        }

        if(request.plAmt()!=null && request.plAmt()<0){
            b.and(base, b.lte("plAmt",200000));
        }else{
            b.and(base, b.gte("plAmt",100000));
        }

        Filter.Expression expression = base.build();
        log.info("Expression {}", expression);

        return this.vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(tradeQuery)
                        .topK(2)
                        .filterExpression(expression)
                        .build());
    }

    @Override
    public List<Document> searchTheoryDocs(AiCommentRequest request) {
        String theoryQuery = buildTheorySearchQuery(request);
        return this.vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(theoryQuery)
                        .topK(2)
                        .filterExpression("type == 'theory'")
                        .build());
    }



    @Override
    public List<Document> searchMarketDocs(AiCommentRequest request) {
        String dateStr = request.tradeDay().toString(); // "2026-02-02"
        return this.vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(dateStr + " 시황")
                        .topK(1)
                        .filterExpression("type == 'market' && date == '" + dateStr + "'")
                        .build());
    }

    @Override
    public List<Document> searchOverallReview(String query) {
        return this.vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(10)
                        .build());
    }

    private String buildTradeSearchQuery(AiCommentRequest request) {
        StringBuilder query = new StringBuilder();
        query.append(request.tradeType());
        return query.toString().trim();
    }

    private String buildTheorySearchQuery(AiCommentRequest request) {
        StringBuilder query = new StringBuilder();

        query.append("트레이더김씨 ").append(request.tradeType());
        query.append("드림로드 ").append(request.tradeType());
        query.append("강의 ").append(request.tradeType());
        query.append("K1 K0 ").append(request.tradeType());
        query.append("상단 중단 하단 ").append(request.tradeType());
        query.append(request.tradeType());

        if (request.plAmt() < 0) {
            query.append(" 손절 원칙 손실 감당");
        } else {
            query.append(" 매수 매도 타점");
        }

        return query.toString().trim();
    }
}
