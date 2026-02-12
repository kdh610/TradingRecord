package com.tradingRecord.tradingRecord.infrastructure.kiwoom;

import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KiwoomRestClient implements StockCompanyApiClient {
    @Value("${trade.api.host}")
    private String host;

    @Value("${trade.api.app_key}")
    private String appKey;

    @Value("${trade.api.app_secret_key}")
    private String appSecretKey;

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
            if(body.totalSellAmount().equals("0")){
                return Optional.empty();
            }

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
