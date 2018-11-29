package me.dellin.stocks;

import me.dellin.Client;
import me.dellin.Offer;

/**
 * StockExchangeInterface
 */
public interface StockExchangeInterface {
    /**
     * Place order in market
     * @param offer offer
     * @return true if offer is added to the market
     */
    boolean makeOffer(Offer offer);

    /**
     * Adds client to market. Now market can accepts offers from that client
     * @param client client
     */
    void registerClient(Client client);

    /**
     * Remove client from market. Client's offers also deleted from market.
     * @param client client
     * @return client or null, if there is no such client
     */
    Client unregisterClient(Client client);

    /**
     * Shows a bit of stats
     * @return stats information
     */
    MarketStats printStats();
}
