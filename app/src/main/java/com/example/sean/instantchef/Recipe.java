package com.example.sean.instantchef;

import org.json.JSONException;
import org.json.JSONObject;

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
    public String name; // recipe name
    public String dateCreated; // date created
    public String createdBy; // author
    public boolean isActive; // pause/resume timer execution
    public int totalRunTimeSeconds;

    public ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public ArrayList<Step> steps = new ArrayList<Step>();

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
        return "{\"id\":" + this.id + ", \"description\":\"" + this.description
                + "\", \"name\":\"" + this.name + "\", \"dateCreated\":\"" + this.dateCreated
                + "\", \"ingredients\":" + ingredients_string + ", \"steps\":" + steps_string + "}";
    }

    public void deleteIngredients() {
        for (int i = 0; i < this.ingredients.size(); i++) {
            this.ingredients.remove(0);
        }
    }

    public void deleteSteps() {
        for (int i = 0; i < this.steps.size(); i++) {
            this.steps.remove(0);
        }
    }

    //import a json_string of recipes into the program.
    public static boolean importRecipes(String json_string) throws JSONException {
        JSONObject json_file = new JSONObject(json_string);

        //there are 2 booleans here because we need to keep track of the local
        //duplicates, and the function needs to know whether there were any
        //duplicates in the whole import process (so we can make a toast).
        boolean any_duplicates = false;
        for (int i = 0; i < json_file.length(); i++) {
            boolean duplicate = false;
            String json_recipe = json_file.getString("recipe" + i);
            Recipe recipe = Recipe.deserialize(json_recipe);
            //TODO: add recipe to the database.
            for (int j = 0; j < MainActivity.recipes.size(); j++) {
                if (recipe.name.equals(MainActivity.recipes.get(j).name)) {
                    any_duplicates = true;
                    duplicate = true;
                }
            }
            if (!duplicate) MainActivity.recipes.add(recipe);
        }
        return any_duplicates;
    }

    //deserializes a JSON string into a recipe object.
    public static Recipe deserialize(String recipe_string) throws JSONException {
        JSONObject json_recipe = new JSONObject(recipe_string);
        Recipe recipe = new Recipe(json_recipe.getString("description"), json_recipe.getString("name"),
                json_recipe.getString("dateCreated"), "");

        JSONObject json_ingredients = json_recipe.getJSONObject("ingredients");
        for (int j = 0; j < json_ingredients.length(); j++) {
            JSONObject json_ingredient = json_ingredients.getJSONObject("ingredient" + j);
            Ingredient ingredient = new Ingredient(json_ingredient.getInt("id"),
                    json_ingredient.getString("description"),
                    json_ingredient.getString("amount"));
            recipe.ingredients.add(ingredient);
        }

        JSONObject json_steps = json_recipe.getJSONObject("steps");
        for (int j = 0; j < json_steps.length(); j++) {
            JSONObject json_step = json_steps.getJSONObject("step" + j);
            Step step = new Step(json_step.getInt("id"),
                    json_step.getString("description"),
                    json_step.getInt("startTime"),
                    json_step.getInt("length"));
            recipe.steps.add(step);
        }
        Recipe.calculate_total_runtime(recipe);
        return recipe;
    }

    //calculates the total run time for a passed recipe and stores it.
    public static void calculate_total_runtime(Recipe recipe) {
        int maxVal = 0;
        for (int i = 0; i < recipe.steps.size(); ++i) {
            int tempVal = recipe.steps.get(i).startTime + recipe.steps.get(i).length;
            if (tempVal > maxVal) {
                maxVal = tempVal;
            }
        }
        recipe.totalRunTimeSeconds = maxVal;
    }
}
