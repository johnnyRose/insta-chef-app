package com.example.sean.instantchef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by John Rosewicz on 4/8/2015.
 */
public class Recipe implements Serializable {
    public static int recipe_index = 0;

    public int id; // database integration
    public String description; // description of recipe
    public String imagePath; // path to image on web
    public String name; // recipe name
    public String dateCreated; // date created
    public String createdBy; // author
    public String totalTime; //total time to run the recipe as a string.
    public boolean isActive; // pause/resume timer execution

    public ArrayList<Ingredient> ingredients;
    public ArrayList<Step> steps;

    public Recipe(String description, String name, String dateCreated, String createdBy) {
        this.description = description;
        this.name = name;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.id = recipe_index++;
    }

    public Recipe() {
        this.dateCreated = new Date().toString(); // current date and time
        this.isActive = false;
        this.ingredients = new ArrayList<Ingredient>();
        this.id = recipe_index++;
    }

    public void start() {
        this.isActive = true;



    }

    public void removeIngredients() {

    }

    public void togglePause() {
        this.isActive = !this.isActive;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    public String serialize() {
        /*

        haven't quite figured this out. to avoid circular serialization however,
        before serialization we will need to set each timer's recipe to null
        just before serialization occurs. when we de-serialize, every timer's
        recipe will need to be set to the main recipe. i think there is a cleaner
        way to do this, but it isn't inherently obvious to me right now.



        problem: when Recipe.isActive is false, all timers need to pause. currently,
        this is implemented by checking the recipe's isActive boolean before every
        loop, while the timer's value is still greater than 0. however, having a
        recipe on timers and timers on recipes will make it impossible to serialize.

        solution: use a database to store recipes, with a single foreign key to timer.ID.
        each timer contains a recursive one-to-many FK to timer.ID. each timer will also
        contain a FK of the recipe.ID.

        i wonder if android studio has a package equivalent to Entity Framework.

        */
        return "";
    }
}
