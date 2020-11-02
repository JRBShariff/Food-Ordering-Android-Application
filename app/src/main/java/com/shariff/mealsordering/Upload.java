package com.shariff.mealsordering;

import com.google.firebase.database.Exclude;

public class Upload {
    private String Name;
    private  String ImageUrl;
    private String Price;
    private String Category;
    private String Key;

    public Upload(){ }

    public Upload(String name, String mprice, String cat,String mImageUrl) {
        this.Name = name;
        this.ImageUrl = mImageUrl;
        this.Price=mprice;
        this.Category = cat;
    }
    @Exclude
    public String getKey() {
        return Key;
    }
    @Exclude
    public void setKey(String key) {
        Key = key;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String mName) {
        this.Name = mName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.ImageUrl = mImageUrl;
    }
}
