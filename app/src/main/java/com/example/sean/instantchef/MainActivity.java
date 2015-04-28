package com.example.sean.instantchef;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    //storing recipes in an arraylist for now, just to simplify the code.
    //we can tie this into a persistent database fairly easy later on.
    public static ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: delete after testing.
        //set up a dummy recipe to make sure the recipe adapter is working.
        /*if (recipes.size() == 0) {
            Recipe test = new Recipe("test recipe", "test recipe", "", "sean");
            test.totalTime = "10 minutes";
            test.ingredients.add(new Ingredient(0, "pizza", "one"));
            test.ingredients.add(new Ingredient(1, "pepperoni", "16"));
            test.steps.add(new Step(0, "boil", 0, 40));
            test.steps.add(new Step(1, "boil more", 30, 40));
            recipes.add(test); recipes.add(test); //add it twice to make sure multiple recipes work.
        }*/
    }

    public void go_to_import_screen(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ImportExportActivity.class);
        startActivity(intent);
    }

    public void go_to_recipe_list(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, RecipeListActivity.class);
        startActivity(intent);
    }

    public void go_to_create_edit_recipe(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, CreateEditActivity.class);
        intent.putExtra("sean_and_john.edit_recipe.edit", false);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
