package com.example.ecommerce;

public class forproduct {
    forproduct(){

    }

    public forproduct(String name, String price, String filepath, String category, String randomKey) {
        this.name = name;
        this.price = price;
        this.filepath = filepath;
        this.category = category;
        this.randomKey = randomKey;
    }

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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
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

    private String name,price,filepath, category,randomKey;

}
