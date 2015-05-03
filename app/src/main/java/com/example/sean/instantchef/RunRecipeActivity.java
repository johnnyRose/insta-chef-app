package com.example.sean.instantchef;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jmr1792 on 4/27/2015.
 */
public class RunRecipeActivity extends Activity {

    public Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_recipe);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("sean_and_john.run_recipe.info");
        this.recipe = MainActivity.recipes.get((int)id);

        ((TextView)findViewById(R.id.RecipeName)).setText(this.recipe.name);
        ((TextView)findViewById(R.id.timeRemaining)).setText(Integer.toString(this.recipe.totalRunTimeSeconds));
    }

    public void playPause(View view) {
        ((TextView)findViewById(R.id.playPause)).setText(this.recipe.isActive ? "Start" : "Pause");
        this.recipe.isActive = !this.recipe.isActive;
    }
}