package com.playground.api.integrations.adapters.dtos;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class GetOpenExchangeRatesResponse {
    Rates rates;

    @Value
    @Builder
    public static class Rates {
        BigDecimal USD;
        BigDecimal EUR;
        BigDecimal JPY;
        BigDecimal CAD;
        BigDecimal GBP;
        BigDecimal AUD;
    }
}
