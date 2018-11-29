package me.dellin;

import me.dellin.stocks.StockExchangeFactory;
import me.dellin.stocks.StockExchangeInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MainApplication {

    private static Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        log.info("App started with args: {}", Arrays.toString(args));
        String paths[] = {"clients.txt", "orders.txt", "results.txt"};
        if (args.length == 3) {
            paths = args;
            log.info("Use values from commandline...");
        } else {
            log.info("Default path variables are in use: {}", Arrays.toString(paths));
        }
        List<Client> clients = Utils.getClients(paths[0]);
        List<Offer> offers = Utils.getOffers(paths[1]);
        log.info("Data loaded, we have {} client(s) and {} offer(s)", clients.size(), offers.size());

        StockExchangeInterface stockExchange = StockExchangeFactory.getFullMatchStockExchange();
        clients.forEach(stockExchange::registerClient);
        stockExchange.printStats();
        for (Offer offer : offers) {
            stockExchange.makeOffer(offer);
        }
        stockExchange.printStats();
        clients.forEach(stockExchange::unregisterClient);
        stockExchange.printStats();

        clients.forEach(client -> log.info("Client state: {}", client));
        log.info("Save client state...");
        Utils.setClients(paths[2], clients);
        log.info("Close...");
    }
}
