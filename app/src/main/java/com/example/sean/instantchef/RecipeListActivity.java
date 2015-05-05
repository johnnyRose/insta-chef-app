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

import java.util.ArrayList;

public class RecipeListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //set up the list adapter to show all the recipes.
        RecipeAdapter myAdapter = new RecipeAdapter(this);
        ListView listView = (ListView) findViewById(R.id.recipe_list_view);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startRecipe(id);
            }
        });
        //myAdapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //editing a recipe, set the 2 lists on the next screen.
                //make a deep copy so the original stays intact.
                CreateEditActivity.ingredients = new ArrayList<>();
                CreateEditActivity.steps = new ArrayList<>();
                for (int i = 0; i < MainActivity.recipes.get(position).ingredients.size(); i++) {
                    CreateEditActivity.ingredients.add(MainActivity.recipes.get(position).ingredients.get(i));
                }
                for (int i = 0; i < MainActivity.recipes.get(position).steps.size(); i++) {
                    CreateEditActivity.steps.add(MainActivity.recipes.get(position).steps.get(i));
                }

                Intent intent = new Intent(getBaseContext(), CreateEditActivity.class);
                MainActivity.editing = true;
                intent.putExtra("sean_and_john.edit_recipe.incoming", true);
                intent.putExtra("sean_and_john.edit_recipe.number", position);
                startActivity(intent);
                return true;
            }
        });
    }

    public void startRecipe(long id) {
        Intent intent = new Intent(this, RunRecipeActivity.class);
        intent.putExtra("sean_and_john.run_recipe.info", id);
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

    public class RecipeAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public RecipeAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return MainActivity.recipes.size();
        }
        @Override
        public Object getItem(int arg0) {
            return MainActivity.recipes.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) RecipeListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.recipe_adapter_view, arg2, false);
            }
            TextView nameView = (TextView)arg1.findViewById(R.id.nameView);
            TextView timeView = (TextView)arg1.findViewById(R.id.timeView);

            Recipe recipe = MainActivity.recipes.get(arg0);
            nameView.setText(recipe.name);
            timeView.setText("Total cooking time: " + Integer.toString(recipe.totalRunTimeSeconds));

            return arg1;
        }
    }
}
