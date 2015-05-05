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
    public static boolean editing = false;
    public static int recipe_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //new recipe, clear the lists/vars on the next screen.
        CreateEditActivity.ingredients = new ArrayList<>();
        CreateEditActivity.steps = new ArrayList<>();
        CreateEditActivity.new_recipe_name = "";
        CreateEditActivity.new_recipe_description = "";
        CreateEditActivity.new_recipe_createdby = "";

        editing = false;

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
