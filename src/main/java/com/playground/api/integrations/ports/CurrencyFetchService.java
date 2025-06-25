package com.playground.api.integrations.ports;

import com.playground.api.enums.CurrencyType;
import com.playground.api.exceptions.ApiException;
import com.playground.api.integrations.ports.dtos.GetExchangeRatesResponse;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyFetchService {
    GetExchangeRatesResponse getExchangeRates(CurrencyType baseCurrency) throws ApiException;
}
