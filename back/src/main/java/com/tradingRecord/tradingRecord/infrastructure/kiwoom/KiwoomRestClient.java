package com.tradingRecord.tradingRecord.infrastructure.kiwoom;

import com.tradingRecord.tradingRecord.application.RateLimiterManager;
import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import com.tradingRecord.tradingRecord.presentation.dto.MinuteCandleRequest;
import com.tradingRecord.tradingRecord.presentation.dto.OrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeLogRequest;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KiwoomRestClient implements StockCompanyApiClient {
    @Value("${trade.api.host}")
    private String host;

    @Value("${trade.api.app_key}")
    private String appKey;

    @Value("${trade.api.app_secret_key}")
    private String appSecretKey;

    private final RateLimiterManager rateLimiterManager;
    private RestClient restClient = RestClient.create();

    @Override
    public Optional<KiwoomTradeDiaryResponse> requestTradeLog(TradeLogRequest request) {
        String endpoint = "/api/dostk/acnt";
        String contYn = "Y";
        String nextKey = "";
        String accessToken = login();
        KiwoomTradeDiaryResponse body = null;

        while("Y".equals(contYn)){
            ResponseEntity<KiwoomTradeDiaryResponse> response = restClient.post()
                    .uri(host + endpoint)
                    .header("authorization", "Bearer " + accessToken)
                    .header("cont-yn", contYn)
                    .header("next-key", nextKey)
                    .header("api-id", "ka10170")
                    .body(request)
                    .retrieve()
                    .toEntity(KiwoomTradeDiaryResponse.class);

            body = response.getBody();

            contYn = response.getHeaders().getFirst("cont-yn");
            nextKey = response.getHeaders().getFirst("next-key");

            if (contYn == null || nextKey == null) break;
        }

        return Optional.ofNullable(body);
    }

    @Override
    public Optional<List<KiwoomOrderLogItem>> requestOrderLog(OrderLogRequest request) {
        String endpoint = "/api/dostk/acnt";
        String contYn = "Y";
        String nextKey = "";
        String accessToken = login();

        List<KiwoomOrderLogItem> orderLogs = new ArrayList<>();
        KiwoomOrderLogResponse body = null;

        while("Y".equals(contYn)){
            ResponseEntity<KiwoomOrderLogResponse> response = restClient.post()
                    .uri(host + endpoint)
                    .header("authorization", "Bearer " + accessToken)
                    .header("cont-yn", contYn)
                    .header("next-key", nextKey)
                    .header("api-id", "kt00009")
                    .body(request)
                    .retrieve()
                    .toEntity(KiwoomOrderLogResponse.class);

            body = response.getBody();
            log.info("OrderLog Result {}", body);
            if(body!=null){
                orderLogs.addAll(body.orderLogItems());
            }

            contYn = response.getHeaders().getFirst("cont-yn");
            nextKey = response.getHeaders().getFirst("next-key");

            if (contYn == null || nextKey == null) break;
        }

        return Optional.ofNullable(orderLogs);
    }

    @Override
    public Optional<KiwoomDailyRealProfitResponse> requestDailyRealProfit(DailyRealProfitRequest request) {
        String endpoint = "/api/dostk/acnt";
        String contYn = "Y";
        String nextKey = "";
        String accessToken = login();

        KiwoomDailyRealProfitResponse body = null;

        while("Y".equals(contYn)){
            ResponseEntity<KiwoomDailyRealProfitResponse> response = restClient.post()
                    .uri(host + endpoint)
                    .header("authorization", "Bearer " + accessToken)
                    .header("cont-yn", contYn)
                    .header("next-key", nextKey)
                    .header("api-id", "ka10074")
                    .body(request)
                    .retrieve()
                    .toEntity(KiwoomDailyRealProfitResponse.class);

            body = response.getBody();
            log.info("당일 실현손익 {}", body);
//            if(body.totalSellAmount().equals("0")){
//                return Optional.empty();
//            }

            contYn = response.getHeaders().getFirst("cont-yn");
            nextKey = response.getHeaders().getFirst("next-key");

            if (contYn == null || nextKey == null) break;
        }

        return Optional.ofNullable(body);
    }

    @Override
    public Optional<KiwoomDailyStockProfitResponse> requestDailyStockProfit(DailyStockProfitRequest request) {
        String endpoint = "/api/dostk/acnt";
        String contYn = "Y";
        String nextKey = "";
        String accessToken = login();

        KiwoomDailyStockProfitResponse body = null;

        while("Y".equals(contYn)){
            ResponseEntity<KiwoomDailyStockProfitResponse> response = restClient.post()
                    .uri(host + endpoint)
                    .header("authorization", "Bearer " + accessToken)
                    .header("cont-yn", contYn)
                    .header("next-key", nextKey)
                    .header("api-id", "ka10072")
                    .body(request)
                    .retrieve()
                    .toEntity(KiwoomDailyStockProfitResponse.class);

            if(body==null){
                body=response.getBody();
            }else {

                body.dtStkDivRlztPl().addAll(response.getBody().dtStkDivRlztPl());
            }
            log.info("날짜 {}", request.strtDt());
            log.info("일자별종목별실현손익요청_일자 {}", body);
            contYn = response.getHeaders().getFirst("cont-yn");
            nextKey = response.getHeaders().getFirst("next-key");

            if (contYn == null || nextKey == null) break;
        }

        return Optional.ofNullable(body);
    }

    @Override
    public Optional<KiwoomMinuteCandleResponse> requestMinuteCandle(MinuteCandleRequest request) {
        String endpoint = "/api/dostk/chart";
        String contYn = "Y";
        String nextKey = "";
        String accessToken = login();

        KiwoomMinuteCandleResponse body = null;

        while("Y".equals(contYn) ){
            ConsumptionProbe probe = rateLimiterManager.probe();
            if (!probe.isConsumed()) {
                long waitTimeMs = probe.getNanosToWaitForRefill() / 1_000_000;
                log.info("레이트 리밋");
                try {
                    Thread.sleep(Math.max(waitTimeMs, 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("대기 중 흐름이 끊겼습니다.", e);
                }
            }

            ResponseEntity<KiwoomMinuteCandleResponse> response = restClient.post()
                    .uri(host + endpoint)
                    .header("authorization", "Bearer " + accessToken)
                    .header("cont-yn", contYn)
                    .header("next-key", nextKey)
                    .header("api-id", "ka10080")
                    .body(request)
                    .retrieve()
                    .toEntity(KiwoomMinuteCandleResponse.class);

            if(body==null){
                body=response.getBody();

            }else {

                body.chartItems().addAll(response.getBody().chartItems());
            }
            String lastItemTime = response.getBody().chartItems().getLast().cntrTm();
            // 앞 8자리 "20260211"만 추출
            String datePart = lastItemTime.substring(0, 8);

            // 기본 제공되는 BASIC_ISO_DATE(yyyyMMdd) 포맷터 사용
            LocalDate fetchedDate = LocalDate.parse(datePart, DateTimeFormatter.BASIC_ISO_DATE);
            if(fetchedDate.isBefore(request.start())){
                break;
            }

            contYn = response.getHeaders().getFirst("cont-yn");
            nextKey = response.getHeaders().getFirst("next-key");

            if (contYn == null || nextKey == null) break;

        }

        return Optional.ofNullable(body);
    }

    private String login() {
        String oauthEndpoint = "/oauth2/token";
        TokenRequest requestBody = new TokenRequest("client_credentials", appKey, appSecretKey);

        TokenResponse response = restClient.post()
                .uri(host + oauthEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, res) -> {
                    throw new RuntimeException("키움 API 인증 실패: " + res.getStatusCode());
                })
                .body(TokenResponse.class);
        return response != null ? response.token() : null;
    }




}
