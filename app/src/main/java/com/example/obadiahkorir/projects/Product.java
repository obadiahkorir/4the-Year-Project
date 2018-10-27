package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/1/2018.
 */

public class Product {
    private int id;
    private String title;
    private String shortdesc;
    private String rating;
    private String price;
    private String image;

    public Product(int id, String title, String shortdesc, String rating, String price, String image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}