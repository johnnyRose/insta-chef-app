package com.example.sean.instantchef;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jmr1792 on 4/27/2015.
 */
public class RunRecipeActivity extends Activity {

    public static Recipe recipe;
    public IngredientAdapter ingredientAdapter;
    public TimerAdapter timerAdapter;
    private boolean started = false;
    public LinearLayout stepsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_recipe);

        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("sean_and_john.run_recipe.info");
        RunRecipeActivity.recipe = MainActivity.recipes.get((int)id);

        ((TextView)findViewById(R.id.runRecipeTitleView)).setText(RunRecipeActivity.recipe.name);
        ((TextView)findViewById(R.id.recipeTotalTimeView)).setText("Instructions, total time: "
                + Integer.toString(RunRecipeActivity.recipe.totalRunTimeSeconds / 60) + " minutes.");

        //set up the adapters
        ingredientAdapter = new IngredientAdapter(this);
        ListView listView1 = (ListView) findViewById(R.id.recipeIngredientListView);
        listView1.setAdapter(ingredientAdapter);

        timerAdapter = new TimerAdapter(this);
        ListView listView2 = (ListView) findViewById(R.id.recipeStepListView);
        listView2.setAdapter(timerAdapter);

        //TODO: populate the stepsDisplay linear layout with the timers.
    }

    public void playPause(View view) {
        if (!started) {
            //hide the recipeDisplay linear layout and show the stepsDisplay layout instead if this
            //is the first time the user presses the start/pause button.
            LinearLayout infoLayout = (LinearLayout) findViewById(R.id.recipeDisplay);
            infoLayout.setVisibility(View.GONE);
            stepsDisplay = (LinearLayout) findViewById(R.id.stepDisplay);
            stepsDisplay.setVisibility(View.VISIBLE);
            started = true;
        }
        ((TextView)findViewById(R.id.playPause)).setText(this.recipe.isActive ? "Resume" : "Pause");
        this.recipe.isActive = !this.recipe.isActive;
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
            return RunRecipeActivity.recipe.ingredients.size();
        }
        @Override
        public Object getItem(int arg0) {
            return RunRecipeActivity.recipe.ingredients.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) RunRecipeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.ingredient_list_view, arg2, false);
            }
            TextView nameView = (TextView)arg1.findViewById(R.id.nameView);
            TextView amountView = (TextView)arg1.findViewById(R.id.amountView);

            Ingredient ingredient = RunRecipeActivity.recipe.ingredients.get(arg0);
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
            return RunRecipeActivity.recipe.steps.size();
        }
        @Override
        public Object getItem(int arg0) {
            return RunRecipeActivity.recipe.steps.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) RunRecipeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.timer_list_view, arg2, false);
            }
            TextView descriptionView = (TextView)arg1.findViewById(R.id.descriptionView);
            TextView timeLeftView = (TextView)arg1.findViewById(R.id.timeLeftView);
            Step step = RunRecipeActivity.recipe.steps.get(arg0);

            descriptionView.setText(step.description);
            timeLeftView.setText(step.length + " seconds");

            return arg1;
        }
    }
}