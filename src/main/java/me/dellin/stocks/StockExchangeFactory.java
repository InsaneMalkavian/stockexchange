package me.dellin.stocks;

public abstract class StockExchangeFactory {
    public static StockExchangeInterface getFullMatchStockExchange() {
        return new FullMatchStockExchange();
    }
}
