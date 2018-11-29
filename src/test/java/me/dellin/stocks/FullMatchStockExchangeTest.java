package me.dellin.stocks;

import me.dellin.Client;
import me.dellin.Offer;
import me.dellin.Utils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FullMatchStockExchangeTest {
    private static final String OFFER_1 = "C6\ts\tB\t5\t2";
    private static final String OFFER_2 = "C1\ts\tD\t3\t1";
    private static final String OFFER_3 = "C1\ts\tB\t5\t2";
    private static final String OFFER_4 = "C2\tb\tD\t3\t1";
    private static final String CLIENT_1 = "C1\t1000\t130\t240\t760\t320";
    private static final String CLIENT_2 = "C2\t4350\t370\t120\t950\t560";

    private static final String CLIENTS_ORIGINAL = "clients.txt";
    private static final String ORDERS = "orders.txt";

    @Test
    public void testInvalidOffers() {
        StockExchangeInterface stock = StockExchangeFactory.getFullMatchStockExchange();
        assertFalse(stock.makeOffer(null));
        assertFalse(stock.makeOffer(Offer.createOffer(OFFER_1)));
    }

    @Test
    public void testRegisterClients() {
        StockExchangeInterface stock = StockExchangeFactory.getFullMatchStockExchange();

        assertNull(stock.unregisterClient(null));

        Client client1 = Client.createClient(CLIENT_1);
        stock.registerClient(client1);
        assertEquals(stock.unregisterClient(client1).getName(), client1.getName());
        assertNull(stock.unregisterClient(client1));
    }

    @Test
    public void testPlaceOfferAndLeave() {
        StockExchangeInterface stock = StockExchangeFactory.getFullMatchStockExchange();

        Client client1 = Client.createClient(CLIENT_1);
        stock.registerClient(client1);
        assertFalse(stock.makeOffer(Offer.createOffer(OFFER_1)));
        assertTrue(stock.makeOffer(Offer.createOffer(OFFER_2)));

        MarketStats stats = stock.printStats();
        assertEquals(1, stats.activeOrders);
        assertEquals(client1.getMoney(), stats.totalMoney);
        assertEquals(0, stats.acceptedDeals);
        stock.unregisterClient(client1);

        stats = stock.printStats();
        assertEquals(0, stats.activeOrders);
        assertEquals(0, stats.totalMoney);
        assertEquals(0, stats.acceptedDeals);
    }

    @Test
    public void testPerformDeal() {
        StockExchangeInterface stock = StockExchangeFactory.getFullMatchStockExchange();

        Client client1 = Client.createClient(CLIENT_1);
        stock.registerClient(client1);
        Client client2 = Client.createClient(CLIENT_2);
        stock.registerClient(client2);
        int totalMoneyStart = stock.printStats().totalMoney;

        stock.makeOffer(Offer.createOffer(OFFER_2));
        stock.makeOffer(Offer.createOffer(OFFER_3));
        stock.makeOffer(Offer.createOffer(OFFER_4));
        MarketStats statsEnd = stock.printStats();
        assertEquals(statsEnd.totalMoney, totalMoneyStart);

        assertEquals(1, statsEnd.activeOrders);
        assertEquals(1, statsEnd.acceptedDeals);
    }

    @Test
    public void testTradingDay() {
        StockExchangeInterface stockExchange = StockExchangeFactory.getFullMatchStockExchange();
        List<Client> clients = Utils.getClients(CLIENTS_ORIGINAL);
        List<Offer> offers = Utils.getOffers(ORDERS);
        clients.forEach(stockExchange::registerClient);
        MarketStats statsStart = stockExchange.printStats();
        offers.forEach(stockExchange::makeOffer);
        MarketStats statsEnd = stockExchange.printStats();
        clients.forEach(stockExchange::unregisterClient);
        assertEquals(statsStart.totalMoney, statsEnd.totalMoney);
        assertEquals(statsEnd.acceptedDeals * 2 + statsEnd.activeOrders, offers.size());

    }
}
