package com.playground.api.integrations.adapters;

import com.playground.api.enums.CurrencyType;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.integrations.adapters.dtos.GetOpenExchangeRatesResponse;
import com.playground.api.integrations.ports.CurrencyFetchService;
import com.playground.api.integrations.ports.dtos.GetExchangeRatesResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenExchangeRatesCurrencyFetchService implements CurrencyFetchService {
    private final WebClient currencyApiClient;

    public OpenExchangeRatesCurrencyFetchService(@Qualifier("currencyApiClient") WebClient currencyApiClient) {
        this.currencyApiClient = currencyApiClient;
    }

    @Override
    public GetExchangeRatesResponse getExchangeRates(CurrencyType baseCurrency) {
        GetOpenExchangeRatesResponse response = currencyApiClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/latest.json")
                        .queryParam("base", baseCurrency.name())
                        .build())
                .retrieve()
                .bodyToMono(GetOpenExchangeRatesResponse.class)
                .block();

        if (response == null) {
            throw new ApiException(
                    "Failed to fetch exchange rates",
                    ErrorCode.INFORMATION_NOT_FOUND,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Map<CurrencyType, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyType.USD, response.getRates().getUSD());
        exchangeRates.put(CurrencyType.EUR, response.getRates().getEUR());
        exchangeRates.put(CurrencyType.GBP, response.getRates().getGBP());
        exchangeRates.put(CurrencyType.JPY, response.getRates().getJPY());
        exchangeRates.put(CurrencyType.CAD, response.getRates().getCAD());
        exchangeRates.put(CurrencyType.AUD, response.getRates().getAUD());

        return GetExchangeRatesResponse.builder()
                .baseCurrency(baseCurrency)
                .exchangeRates(exchangeRates)
                .build();
    }
}
