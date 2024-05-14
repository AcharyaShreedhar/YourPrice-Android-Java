package com.shree.yourprice;

public class Item {
    private int id;
    private String name;
    private String price;
    private int imageResource;
    private String details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item(int id, String name, String price, int imageResource, String details) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.details = details;
    }

    public Item(String name, String price, int imageResource, String details) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDetails() {
        return details;
    }
}
