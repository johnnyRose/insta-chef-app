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

    public ArrayList<Ingredient> ingredients = new ArrayList<>();
    public ArrayList<Step> steps = new ArrayList<>();

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

    public void addStep(Step step) {
        this.steps.add(step);
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
        String steps_string = "{";
        String ingredients_string = "{";
        for (int i = 0; i < this.steps.size(); i++) {
            steps_string += "\"step" + i + "\": " + this.steps.get(i).serialize();
            if (i < this.steps.size() - 1) {
                steps_string += ", "; //add a comma after every step except the last.
            }
        }
        steps_string += "}";
        for (int i = 0; i < this.ingredients.size(); i++) {
            ingredients_string += "\"ingredient" + i + "\": " + this.ingredients.get(i).serialize();
            if (i < this.ingredients.size() - 1) {
                ingredients_string += ", "; //add a comma after every ingredient except the last.
            }
        }
        ingredients_string += "}";
        //System.out.println("steps = " + steps_string);
        //System.out.println("ingredients = " + ingredients_string);
        return "{\"id\":" + this.id + ", \"description\":\"" + this.description
                + "\", \"name\":\"" + this.name + "\", \"dateCreated\":\"" + this.dateCreated
                + "\", \"ingredients\":" + ingredients_string + ", \"steps\":" + steps_string + "}";
    }

    public void deleteIngredients() {
        this.ingredients = new ArrayList<>();
    }

    public void deleteSteps() {
        this.steps = new ArrayList<>();
    }
}
