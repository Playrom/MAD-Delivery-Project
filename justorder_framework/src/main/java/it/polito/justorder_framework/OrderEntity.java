package it.polito.justorder_framework;

public class OrderEntity {

        private String orderId;
        private String user;
        private String address;
        private String price;
        private String timestamp;
        private String rider;


    private String[] products;

    public OrderEntity(String orderId, String user, String address, String price, String timestamp, String rider, String[] products) {
        this.orderId = orderId;
        this.user = user;
        this.address = address;
        this.price = price;
        this.timestamp = timestamp;
        this.rider = rider;
        this.products = products;
    }

    public OrderEntity() {
        this("", "", "", "", "", "", null);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }
    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }
}
