package com.example.signuploginfirebase.Cart;

public class ItemCart {
    int image;
    String descripttion;
    String numberOfItem;

    public ItemCart(int image, String descripttion, String numberOfItem) {
        this.image = image;
        this.descripttion = descripttion;
        this.numberOfItem = numberOfItem;
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
