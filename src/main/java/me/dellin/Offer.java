package me.dellin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

public class Offer {
    private static Logger log = LoggerFactory.getLogger(Offer.class);
    private int count;
    private int price;
    private String clientName;
    private OfferType type;
    private String stuff;
    private Date publicDate;
    private Date dealDate;

    private Offer(String clientName, OfferType offerType, String stuff, int price, int count) {
        this.clientName = clientName;
        this.type = offerType;
        this.stuff = stuff;
        this.price = price;
        this.count = count;
    }

    /**
     *
     * Instantiate offer object. Data should contain tab-delimited client name, stuff name and etc values
     * @param data string representation of offer data
     * @return offer object, or null if data is not correct
     */
    public static Offer createOffer(String data) {
        String[] parts = data.split("\t");
        String clientName;
        String stuff;
        OfferType offerType;
        int count;
        int price;
        try {
            clientName = parts[0];
            offerType = parts[1].equals("s") ? OfferType.SELL : parts[1].equals("b") ? OfferType.BUY : null;
            stuff = parts[2];
            price = Integer.parseInt(parts[3]);
            count = Integer.parseInt(parts[4]);
        }
        catch (Exception ex) {
            log.error("unable to create offer, invalid data: {}", data, ex);
            throw ex;
        }
        return new Offer(clientName, offerType, stuff, price, count);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public OfferType getType() {
        return type;
    }

    public void setType(OfferType type) {
        this.type = type;
    }

    public String getStuff() {
        return stuff;
    }

    public void setStuff(String stuff) {
        this.stuff = stuff;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return count == offer.count &&
                price == offer.price &&
                Objects.equals(clientName, offer.clientName) &&
                type == offer.type &&
                Objects.equals(stuff, offer.stuff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, price, clientName, type, stuff);
    }
}
