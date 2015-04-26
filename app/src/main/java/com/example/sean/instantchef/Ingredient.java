package com.example.sean.instantchef;

/**
 * Created by John Rosewicz on 4/8/2015.
 */
public class Ingredient {
    public int id;
    public String description;
    public String amount;

    public Ingredient(int id, String description, String amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }
}
