package com.example.b.class6;

/**
 * Created by b on 1/27/2016.
 */
public class Item {
    public String itemName;
    public enum Category {produce, canned, boxed, fresh, deli, other};
    public Category itemCategory;


    public Item(String name, Category category){
        itemName=name;
        itemCategory=category;
    }

    public Item(String name){
        itemName=name;
        itemCategory=Category.other;
    }

}
