package me.dellin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class);
    private String name;
    private int money;
    private Map<String, Integer> counts;

    private Client(String name, int money, Map<String, Integer> counts) {
        this.name = name;
        this.money = money;
        this.counts = new LinkedHashMap<>(counts);
    }

    /**
     * Instantiate client object. Data should contain tab-delimited name, money, stuff count values
     * @param data string representation of client data (name, money, a-stuff count, b-stuff count, etc
     * @return client object, or null if data is not correct
     */
    public static Client createClient(String data) {
        log.info("creating client...{}", data);
        String[] parts = data.split("\t");
        String name;
        int money;
        Map<String, Integer> counts = new LinkedHashMap<>();
        try { // possible fail reasons: invalid number of columns (we expect only 6)
            name = parts[0]; // not an integer passed as amount of money or count
            money = Integer.parseInt(parts[1]);
            counts.put("A", Integer.parseInt(parts[2]));
            counts.put("B", Integer.parseInt(parts[3]));
            counts.put("C", Integer.parseInt(parts[4]));
            counts.put("D", Integer.parseInt(parts[5]));
        }
        catch (Exception ex) {
            log.error("unable to create client, invalid data: {}", data, ex);
            throw ex;
        }
        return new Client(name, money, counts);
    }

    /**
     * Returns name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns amount of money
     * @return amount of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Apply deal to client
     * @param type offer type
     * @param stuff name of stuff
     * @param price price
     * @param count amount of stuff
     */
    public void applyDeal(OfferType type, String stuff, int price, int count) {
        switch (type) {
            case BUY:
                break;
            case SELL:
                count *= -1;
                break;
        }
        money -= (price * count);
        int stuffCount = counts.computeIfAbsent(stuff, x -> 0);
        stuffCount += count;
        counts.put(stuff, stuffCount);
    }

    /**
     * Returns formatted string
     * @return formatted string
     */
    public String presentation() {
        String values = counts.values().stream().map(Object::toString).collect(Collectors.joining("\t"));
        return name + '\t' + money + '\t' + values;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", counts=" + counts +
                '}';
    }
}
