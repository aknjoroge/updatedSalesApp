package com.example.ecommerce;

public class forfeed {
    public forfeed(){

    }

    String name,filepath,category,likes,randomKey;

    public forfeed(String randomKey) {
        this.randomKey = randomKey;
    }

    public forfeed(String name, String filepath, String category, String likes) {
        this.name = name;
        this.filepath = filepath;
        this.category = category;
        this.likes = likes;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
