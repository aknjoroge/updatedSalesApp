package com.example.ecommerce;

public class forcategory {
    forcategory(){

    }
     String name,randomKey,filepath,category;


    public forcategory(String name, String randomKey, String filepath, String category) {
        this.name = name;
        this.category =category;
        this.randomKey = randomKey;
        this.filepath = filepath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
