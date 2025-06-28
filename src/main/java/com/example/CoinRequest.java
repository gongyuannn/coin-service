package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class CoinRequest {
    // use BigDecimal for higher precision
    private final BigDecimal targetAmount;
    private final List<BigDecimal> denominations;

    public CoinRequest(
            @JsonProperty("targetAmount") BigDecimal targetAmount,
            @JsonProperty("denominations") List<BigDecimal> denominations
    ) {
        this.targetAmount = targetAmount;
        this.denominations = denominations;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public List<BigDecimal> getDenominations() {
        return denominations;
    }
}

