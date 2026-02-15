package com.tradingRecord.tradingRecord.infrastructure.kiwoom;

import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TokenRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TokenResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class KiwoomTokenProvider {

    @Value("${trade.api.host}")
    private String host;
    @Value("${trade.api.app_key}")
    private String appKey;
    @Value("${trade.api.app_secret_key}")
    private String appSecretKey;
    private RestClient restClient = RestClient.create();

    private String accessToken;
    @PostConstruct
    public void init(){
        log.info("AccessToken 로그인 최초 요청");
        accessToken = this.requestAccessToken();
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public String requestAccessToken(){
        return this.login();
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
