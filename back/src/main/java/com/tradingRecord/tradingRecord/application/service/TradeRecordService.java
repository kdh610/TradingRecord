package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.SearchTradeResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeRepository;
import com.tradingRecord.tradingRecord.infrastructure.DB.TradeSummary;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.infrastructure.exception.BaseException;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.SearchTradeRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeRecordService {

    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;
    private final TradeRepository tradeRepository;
    private final EmbeddingService embeddingService;

    public TradeDiaryResponse getTradeDiary(LocalDate date){
        TradeDiary tradeDiary = tradeDiaryRepository.findByTradeDay(date)
                .orElseThrow(()-> new BaseException(Code.TRADE_DIARY_NOT_FOUND));

        return TradeDiaryResponse.from(tradeDiary);
    }

    public List<SearchOrderLogResponse> searchOrderLog(SearchOrderLogRequest request){
        List<OrderLog> orderLogs = orderLogRepository.search(request.stkNm(), request.start(), request.end());
        return orderLogs.stream().map(SearchOrderLogResponse::from).toList();
    }

    @Transactional
    public void processTradeWinRateAndSave(TradeRequest requests){
        List<OrderLog> orderLogs = orderLogRepository.findAllById(requests.orderLogIds());
        Trade trade = Trade.create(orderLogs, requests);
        tradeRepository.save(trade);
        saveTradeToVectorStore(trade);
    }

    private void saveTradeToVectorStore(Trade trade) {
        String orderLogSummary = trade.createOrderLogSummary();
        String tradeSummary = trade.createTradeSummary();
        String content = tradeSummary + "\n[타점 요약]: " + orderLogSummary;

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("stkNm", trade.getStkNm());
        metadata.put("tradingType", trade.getTradingType());
        metadata.put("winLose", trade.getWinLose());
        metadata.put("stupid", trade.getStupid());
        metadata.put("tradeDay", trade.getTradeDay().toString());
        metadata.put("plAmt", trade.getPlAmt());
        Document document = new Document(content, metadata);
        embeddingService.saveEmbeddingInfo(document);
    }

    public Page<SearchTradeResponse> searchTrade(SearchTradeRequest request, Pageable pageable){
        Page<Trade> trades = tradeRepository.searchTrade(request, pageable);
        return trades.map(SearchTradeResponse::from);
    }


    @Transactional
    public void deleteTrade(String id){
        tradeRepository.findById(UUID.fromString(id)).orElseThrow(()->new BaseException(Code.TRADE_NOT_FOUND));

        orderLogRepository.detachOrderLogsByTradeId(UUID.fromString(id));

        tradeRepository.deleteTrade(UUID.fromString(id));


    }

    public List<TradeSummary>  getAllTrade(){
        return tradeRepository.findAllProjectedBy().orElseThrow(()->new BaseException(Code.TRADE_NOT_FOUND));
    }


}
