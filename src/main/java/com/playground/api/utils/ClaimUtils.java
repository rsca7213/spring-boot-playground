package com.playground.api.utils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ClaimUtils {
    public String generateClaimNumber(Integer policyNumber, LocalDate claimDate, Integer claimCount) {
        // Return the following format: <claimDate:ddMMyyyy>-<policyNumber>-<claimNumber>
        return String.format(
                "%s-%s-%s",
                claimDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")),
                policyNumber,
                claimCount
        );
    }

    public BigDecimal currencyUfToClp(BigDecimal amount) {
        final BigDecimal conversionRate = new BigDecimal("39235.70");
        BigDecimal conversion = amount.multiply(conversionRate);
        return conversion.setScale(2, RoundingMode.HALF_UP);
    }
}
