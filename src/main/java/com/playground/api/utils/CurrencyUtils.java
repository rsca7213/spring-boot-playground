package com.playground.api.utils;

import com.playground.api.enums.CurrencyType;
import com.playground.api.integrations.ports.CurrencyFetchService;
import com.playground.api.integrations.ports.dtos.GetExchangeRatesResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CurrencyUtils {
    private final CurrencyFetchService currencyFetchService;

    public CurrencyUtils(CurrencyFetchService currencyFetchService) {
        this.currencyFetchService = currencyFetchService;
    }

    public Map<CurrencyType, BigDecimal> getPricesForCurrencies(CurrencyType baseCurrency, BigDecimal amount) {
        GetExchangeRatesResponse exchangeRatesResponse = currencyFetchService.getExchangeRates(baseCurrency);

        return exchangeRatesResponse.getExchangeRates().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry
                                .getValue()
                                .multiply(amount)
                                .setScale(2, RoundingMode.HALF_UP)
                ));
    }
}
