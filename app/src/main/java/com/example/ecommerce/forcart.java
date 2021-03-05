package com.example.ecommerce;

public class forcart {
    private String name,price,category,randomKey,amount,key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public forcart(String name, String price, String category, String randomKey, String amount, String key) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.randomKey = randomKey;
        this.amount = amount;
        this.key = key;
    }
}
