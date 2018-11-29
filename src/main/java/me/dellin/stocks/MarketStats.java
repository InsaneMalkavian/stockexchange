package me.dellin.stocks;

class MarketStats {
    int activeOrders;
    int acceptedDeals;
    int totalMoney;

    public int getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(int activeOrders) {
        this.activeOrders = activeOrders;
    }

    public int getAcceptedDeals() {
        return acceptedDeals;
    }

    public void setAcceptedDeals(int acceptedDeals) {
        this.acceptedDeals = acceptedDeals;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }
}
