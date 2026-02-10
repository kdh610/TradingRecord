package com.tradingRecord.tradingRecord.infrastructure.kiwoom;

import com.tradingRecord.tradingRecord.application.StockApiClient;
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
public class KiwoomRestClient implements StockApiClient {
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

        log.info("token {}", accessToken);
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

        log.info("token {}", accessToken);
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

    private String login() {
        String oauthEndpoint = "/oauth2/token";
        TokenRequest requestBody = new TokenRequest("client_credentials", appKey, appSecretKey);
        log.info("requestBody {}", requestBody);

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
