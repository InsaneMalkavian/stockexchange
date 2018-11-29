package me.dellin;

public enum OfferType {
    BUY,
    SELL;

    public OfferType opposite() {
        return this == BUY ? SELL : BUY;
    }
}
