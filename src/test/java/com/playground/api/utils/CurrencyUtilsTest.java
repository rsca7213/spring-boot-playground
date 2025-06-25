package com.playground.api.utils;

import com.playground.api.enums.CurrencyType;
import com.playground.api.integrations.ports.CurrencyFetchService;
import com.playground.api.integrations.ports.dtos.GetExchangeRatesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CurrencyUtilsTest {
    @Mock
    private CurrencyFetchService currencyFetchService;

    @InjectMocks
    private CurrencyUtils currencyUtils;

    // Values used in the tests
    private final BigDecimal amount = BigDecimal.valueOf(100);
    private GetExchangeRatesResponse exchangeRates;
    private final Map<CurrencyType, BigDecimal> expectedExchangeRates = Map.of(
            CurrencyType.USD, BigDecimal.valueOf(1.0).multiply(amount).setScale(2, RoundingMode.HALF_UP),
            CurrencyType.EUR, BigDecimal.valueOf(0.85).multiply(amount).setScale(2, RoundingMode.HALF_UP),
            CurrencyType.JPY, BigDecimal.valueOf(110.0).multiply(amount).setScale(2, RoundingMode.HALF_UP),
            CurrencyType.CAD, BigDecimal.valueOf(1.25).multiply(amount).setScale(2, RoundingMode.HALF_UP),
            CurrencyType.GBP, BigDecimal.valueOf(0.75).multiply(amount).setScale(2, RoundingMode.HALF_UP),
            CurrencyType.AUD, BigDecimal.valueOf(1.35).multiply(amount).setScale(2, RoundingMode.HALF_UP)
    );

    @BeforeEach
    public void setUp() {
        exchangeRates = GetExchangeRatesResponse.builder()
                .baseCurrency(CurrencyType.USD)
                .exchangeRates(
                        Map.of(
                                CurrencyType.USD, BigDecimal.valueOf(1.0),
                                CurrencyType.EUR, BigDecimal.valueOf(0.85),
                                CurrencyType.JPY, BigDecimal.valueOf(110.0),
                                CurrencyType.CAD, BigDecimal.valueOf(1.25),
                                CurrencyType.GBP, BigDecimal.valueOf(0.75),
                                CurrencyType.AUD, BigDecimal.valueOf(1.35)
                        )
                )
                .build();
    }

    @Test
    public void getPricesForCurrencies_Success() {
        // Mock the currency fetch exchange rates
        Mockito.when(currencyFetchService.getExchangeRates(CurrencyType.USD)).thenReturn(exchangeRates);

        // Call the method to test
        Map<CurrencyType, BigDecimal> prices = currencyUtils.getPricesForCurrencies(
                CurrencyType.USD,
                BigDecimal.valueOf(100)
        );

        // Verify that the currency fetch service was called correctly
        Mockito.verify(currencyFetchService).getExchangeRates(CurrencyType.USD);

        // Verify the results
        Assertions.assertEquals(
                expectedExchangeRates,
                prices
        );

    }
}
