package com.dbtraining.reconx.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Shared state + behaviour for every concrete {@link TradeType}.
 *
 * <p>This class is <strong>package-private</strong> on purpose: the only legal
 * way to obtain a {@code TradeType} outside this package is via a concrete
 * builder or {@link TradeFactory}. The sealed contract lives on the interface;
 * the shared plumbing lives here.</p>
 */
abstract sealed class Trade
        permits EquityTrade, FXTrade, BondTrade, DerivativeTrade {

    protected final TradeRef tradeRef;
    protected final Money notional;
    protected final LocalDate tradeDate;

    protected Trade(TradeRef tradeRef, Money notional, LocalDate tradeDate) {
        this.tradeRef = Objects.requireNonNull(tradeRef, "tradeRef");
        this.notional = Objects.requireNonNull(notional, "notional");
        this.tradeDate = Objects.requireNonNull(tradeDate, "tradeDate");
    }
}