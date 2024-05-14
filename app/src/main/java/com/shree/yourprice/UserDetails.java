package com.shree.yourprice;

public class UserDetails {
    private int id;
    private String name;
    private String username;
    private String contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String address;

    public UserDetails(int id, String name, String username, String contact, String address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }
}

