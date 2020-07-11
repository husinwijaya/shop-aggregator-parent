package com.gmail.nisuhw.model;

public class ProductResult extends ShopResult {
    private String price;
    private String image;

    public ProductResult(ECommercePlatform platform, long id, String name, String url, String price, String image) {
        super(platform, id, name, url);
        this.price = price;
        this.image = image;
    }

    public ProductResult() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
