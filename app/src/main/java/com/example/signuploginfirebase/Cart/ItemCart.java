package com.example.signuploginfirebase.Cart;

public class ItemCart {
    int image;
    String descripttion;
    String numberOfItem;
    String pricePro;
    int id;
    int Product_id;

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int product_id) {
        Product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPricePro() {
        return pricePro;
    }

    public void setPricePro(String pricePro) {
        this.pricePro = pricePro;
    }

    public ItemCart(int id, int product_id, int image, String descripttion, String numberOfItem, String pricePro) {
        this.image = image;
        this.descripttion = descripttion;
        this.numberOfItem = numberOfItem;
        this.pricePro = pricePro;
        this.id = id;
        this.Product_id = product_id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescripttion() {
        return descripttion;
    }

    public void setDescripttion(String descripttion) {
        this.descripttion = descripttion;
    }

    public String getNumberOfItem() {
        return numberOfItem;
    }

    public void setNumberOfItem(String numberOfItem) {
        this.numberOfItem = numberOfItem;
    }
}
