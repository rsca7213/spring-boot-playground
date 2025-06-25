package com.playground.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${integrations.api.currency.url}")
    private String currencyApiUrl;

    @Value("${integrations.api.currency.app-id}")
    private String currencyApiAppId;

    @Bean
    @Qualifier("currencyApiClient")
    public WebClient currencyApiClient() {
        return WebClient.builder()
                .baseUrl(currencyApiUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Authorization", String.format("Token %s", currencyApiAppId))
                .build();
    }
}
