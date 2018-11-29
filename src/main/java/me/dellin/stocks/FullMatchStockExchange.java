package me.dellin.stocks;

import me.dellin.Client;
import me.dellin.Offer;
import me.dellin.OfferType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class FullMatchStockExchange implements StockExchangeInterface {
    private static Logger log = LoggerFactory.getLogger(FullMatchStockExchange.class);
    /* List<Offer> might be not optimal solution. but we can't use set, cause offers can repeat.
    Probably mapping by offertype -> price will be more effective than offertype -> stuff name due to reduced length of lists
     */
    private EnumMap<OfferType, Map<String, List<Offer>>> offers = new EnumMap<>(OfferType.class);
    private Map<String, Client> clients = new HashMap<>();
    private List<Offer> history = new ArrayList<>(); // clean somehow or put in some storage

    FullMatchStockExchange() {
        offers.put(OfferType.BUY, new HashMap<>());
        offers.put(OfferType.SELL, new HashMap<>());
    }

    @Override
    public boolean makeOffer(Offer offer) {
        if (offer == null) {
            log.info("No offer were provided");
            return false;
        }
        Client first = clients.get(offer.getClientName());
        if (first == null) {
            log.info("fake client name, reject offer");
            return false;
        }
        List<Offer> matchList;
        matchList = offers.get(offer.getType().opposite()).computeIfAbsent(offer.getStuff(), stuff -> new ArrayList<>());

        Offer match = null;
        for (Offer placed : matchList) {
            if (placed.getPrice() == offer.getPrice() && placed.getCount() == offer.getCount()) {
                Client second = clients.get(placed.getClientName());
                first.applyDeal(offer.getType(), offer.getStuff(), offer.getPrice(), offer.getCount());
                second.applyDeal(placed.getType(), placed.getStuff(), placed.getPrice(), placed.getCount());
                history.add(placed);
                history.add(offer);
                match = placed;
                break;
            }
        }
        if (match == null) {
            List<Offer> offerList = offers.get(offer.getType()).computeIfAbsent(offer.getStuff(), stuff -> new ArrayList<>());
            offerList.add(offer);
        } else {
            if (!matchList.remove(match)) {
                log.warn("Oops, impossible. It should be here");
            }
        }
        return true;
    }

    @Override
    public void registerClient(Client client) {
        if (client != null) {
            if (clients.containsKey(client.getName())) {
                log.info("client already joined market");
            } else {
                clients.put(client.getName(), client);
            }
        }
    }

    @Override
    public Client unregisterClient(Client client) {
        if (client != null) {
            // drop existing offers
            for (List<Offer> value : offers.get(OfferType.SELL).values()) {
                value.removeIf(v -> v.getClientName().equals(client.getName()));
            }
            for (List<Offer> value : offers.get(OfferType.BUY).values()) {
                value.removeIf(v -> v.getClientName().equals(client.getName()));
            }
            return clients.remove(client.getName()); // remove client from client list
        }
        return null;
    }

    @Override
    public MarketStats printStats() {
        MarketStats stats = new MarketStats();
        int overallMoney = clients.values().stream().mapToInt(Client::getMoney).sum();
        log.info("Deal count(x2): {}, total money: {}", history.size(), overallMoney);
        stats.totalMoney = overallMoney;
        stats.acceptedDeals = history.size() >> 1;
        stats.activeOrders = offers.values().stream().map(x -> x.values()).flatMap(x -> x.stream()).map(x -> x.size()).mapToInt(Integer::intValue).sum();
        return stats;
    }
}
