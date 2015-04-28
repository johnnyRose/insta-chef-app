package com.example.sean.instantchef;

import java.io.Serializable;

/**
 * Created by John Rosewicz on 4/8/2015.
 */
public class Ingredient implements Serializable {
    public int id;
    public String description;
    public String amount;

    public Ingredient(int id, String description, String amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public String serialize() {
        return "{\"id\":" + this.id + ", \"description\":\"" + this.description +  "\", \"amount\":\""
                + this.amount + "\"}";
    }
}
