package com.example.sean.instantchef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sean on 4/25/2015.
 */
public class CreateEditActivity extends ActionBarActivity {
    public static ArrayList<Ingredient> ingredients = new ArrayList<>();
    public static ArrayList<Timer> timers = new ArrayList<>();
    private boolean editing = false;
    private int recipe_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_recipe);
        editing = false;

        //TODO: delete this later. create a dummy timer and ingredient to test that the adapters work.
        /*ingredients.add(new Ingredient(0, "carrots, chopped", "1 cup"));
        Timer timer = new Timer();
        timer.startDescription = "add carrots, boil until melted";
        timer.secondsRemaining = 60;
        timers.add(timer);*/

        //set up both adapters to show ingredients and steps(aka timers).
        IngredientAdapter ingredientAdapter = new IngredientAdapter(this);
        ListView listView1 = (ListView) findViewById(R.id.ingredientListView);
        listView1.setAdapter(ingredientAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: decide what behavior we want when the user clicks an ingredient.
            }
        });
        TimerAdapter timerAdapter = new TimerAdapter(this);
        ListView listView2 = (ListView) findViewById(R.id.timerListView);
        listView2.setAdapter(timerAdapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: decide what behavior we want when the user clicks a timer.
            }
        });
        ingredientAdapter.notifyDataSetChanged();

        //TODO: get the intent and see if we're editing a previous recipe or
        //creating one from scratch. if editing, we'll need to pre-fill
        //all the boxes on this screen and change the title to "Edit Recipe".
    }

    public void add_ingredient(View view) {
        //TODO: implement a dialog to add ingredients
        Intent intent = new Intent(this, AddIngredientsActivity.class);
        startActivity(intent);
    }

    public void add_step(View view) {
        //add a timer to the recipe, the first timer will be the main.
        Intent intent = new Intent(this, AddTimersActivity.class);
        startActivity(intent);
    }

    public void save_recipe(View view) {

        TextView nameEditText = (TextView)findViewById(R.id.nameEditText);
        TextView descriptionEditText = (TextView)findViewById(R.id.descriptionEditText);
        TextView createdByEditText = (TextView)findViewById(R.id.createdByEditText);

        String given_name = nameEditText.getText().toString();
        String given_description = descriptionEditText.getText().toString();
        String given_created_by = createdByEditText.getText().toString();

        //bare-minimum to create a recipe is a name, 1 ingredient and 1 timer.
        if (given_name.equals("") || ingredients.size() == 0 || timers.size() == 0) {
            Toast.makeText(this, "Not enough information to save the recipe. You must"
                    + " have at least 1 ingredient, 1 step, and a name.", Toast.LENGTH_LONG).show();
            return;
        }

        if (editing) {
            Recipe recipe = MainActivity.recipes.get(recipe_number);
            recipe.name = given_name;
            recipe.description = given_description;
            recipe.createdBy = given_created_by;
        } else {
            Recipe recipe = new Recipe(given_description, given_name, new Date().toString(), given_created_by);
            //TODO: add recipe to the database.
            MainActivity.recipes.add(recipe);
        }
        //TODO: add timers and ingredients to the recipe.

        //go to the main screen after saving.
        Intent intent = new Intent(this, MainActivity.class);
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

    public class IngredientAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public IngredientAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return ingredients.size();
        }
        @Override
        public Object getItem(int arg0) {
            return ingredients.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) CreateEditActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.ingredient_list_view, arg2, false);
            }
            TextView nameView = (TextView)arg1.findViewById(R.id.nameView);
            TextView amountView = (TextView)arg1.findViewById(R.id.amountView);

            Ingredient ingredient = ingredients.get(arg0);
            nameView.setText(ingredient.description);
            amountView.setText(ingredient.amount);

            return arg1;
        }
    }

    public class TimerAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public TimerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return timers.size();
        }
        @Override
        public Object getItem(int arg0) {
            return timers.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) CreateEditActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.timer_list_view, arg2, false);
            }
            TextView descriptionView = (TextView)arg1.findViewById(R.id.descriptionView);
            TextView timeLeftView = (TextView)arg1.findViewById(R.id.timeLeftView);
            Timer timer = timers.get(arg0);
            descriptionView.setText(timer.startDescription);
            timeLeftView.setText(timer.secondsRemaining + " seconds");

            return arg1;
        }
    }

}
