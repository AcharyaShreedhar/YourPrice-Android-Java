package com.shree.yourprice;

public class CartItem {
    private String productName;
    private String productPrice;
    private int productImage;
    private String details;

    public CartItem(String productName, String productPrice, int productImage, String details) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.details = details;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
