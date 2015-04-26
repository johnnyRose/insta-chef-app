package com.example.sean.instantchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by sean on 4/25/2015.
 */
public class CreateEditActivity extends ActionBarActivity {
    public ArrayList<Recipe> ingredients = new ArrayList<>();
    public ArrayList<Recipe> steps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_recipe);

        //TODO: get the intent and see if we're editing a previous recipe or
        //creating one from scratch. if editing, we'll need to pre-fill
        //all the boxes on this screen and change the title to "Edit Recipe".
        Intent intent = new Intent();
    }

    public void add_ingredient(View view) {
        //TODO: implement
    }

    public void add_step(View view) {

    }

    public void save_recipe(View view) {

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
