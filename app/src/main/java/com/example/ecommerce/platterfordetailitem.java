package com.example.ecommerce;

public class platterfordetailitem {
    platterfordetailitem(){

    }
    String price,name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public platterfordetailitem(String price, String name) {
        this.price = price;
        this.name = name;
    }
}


