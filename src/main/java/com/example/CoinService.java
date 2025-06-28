package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.*;

public class CoinService {
    // allowed range in dollars
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10_000.00);

    public List<BigDecimal> computeMinCoins(CoinRequest req) {
        BigDecimal remaining = req.getTargetAmount()
                .setScale(2, RoundingMode.UNNECESSARY);

        // validate range
        if (remaining.compareTo(MIN_AMOUNT) < 0 || remaining.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Target amount out of range.");
        }

        // prep and sort denominations in asc order
        List<BigDecimal> coins = new ArrayList<>(req.getDenominations());
        coins.sort(Comparator.reverseOrder());

        // greedy selection
        List<BigDecimal> result = new ArrayList<>();
        for (BigDecimal coin : coins) {
            if (coin.compareTo(BigDecimal.ZERO) <= 0)
                continue;

            // count how many coins particular value can "fit"
            BigDecimal[] division = remaining.divideAndRemainder(coin);
            int count = division[0].intValue(); // get count
            remaining = division[1];
            for (int i = 0; i < count; i++) {
                result.add(coin);
            }
        }

        // check if exact amount can be formed
        if (remaining.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(
                    "Cannot form target amount. Remaining: " + remaining
            );
        }

        // return coins in ascending order
        result.sort(Comparator.naturalOrder());
        return result;
    }

}
