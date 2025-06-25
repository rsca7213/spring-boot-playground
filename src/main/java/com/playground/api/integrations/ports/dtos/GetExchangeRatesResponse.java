package com.playground.api.integrations.ports.dtos;

import com.playground.api.enums.CurrencyType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class GetExchangeRatesResponse {
    CurrencyType baseCurrency;
    Map<CurrencyType, BigDecimal> exchangeRates;
}
