package com.example.sean.instantchef;

import org.json.JSONArray;
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
    public String imagePath; // path to image on web
    public String name; // recipe name
    public String dateCreated; // date created
    public String createdBy; // author
    public String totalTime; //total time to run the recipe as a string.
    public boolean isActive; // pause/resume timer execution

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

    //import a json_string of recipes into the program.
    public static void importRecipes(String json_string) throws JSONException {
        JSONObject json_file = new JSONObject(json_string);

        for (int i = 0; i < json_file.length(); i++) {
            String json_recipe = json_file.getString("recipe" + i);
            Recipe recipe = Recipe.deserialize(json_recipe);
            //TODO: add recipe to the database.
            MainActivity.recipes.add(recipe);
        }
    }

    //deserializes a JSON string into a recipe object.
    public static Recipe deserialize(String recipe_string) throws JSONException {
        JSONObject json_recipe = new JSONObject(recipe_string);
        Recipe recipe = new Recipe(json_recipe.getString("description"), json_recipe.getString("name"),
                json_recipe.getString("dateCreated"), "");

        //JSONObject json_ingredients = new JSONObject(json_file.getString("ingredients"));
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
        return recipe;
    }
}
