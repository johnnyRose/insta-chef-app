package com.example.sean.instantchef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jmr1792 on 4/27/2015.
 */
public class RunRecipeActivity extends Activity {

    public static Recipe recipe;
    public IngredientAdapter ingredientAdapter;
    public TimerAdapter timerAdapter;
    public ActiveTimerAdapter runningTimerAdapter;
    private boolean started = false;
    private boolean paused = false;
    public LinearLayout stepsDisplay;
    public CountDownTimer countDown;
    public int secondsRemaining;
    int timeElapsed = 0;
    public ArrayList<Step> steps = new ArrayList<>();
    public ArrayList<CountDownTimer> timers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_recipe);

        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("sean_and_john.run_recipe.info");
        RunRecipeActivity.recipe = MainActivity.recipes.get((int)id);
        secondsRemaining = RunRecipeActivity.recipe.totalRunTimeSeconds;
        long minutes = secondsRemaining / 60;
        long seconds = (secondsRemaining - (minutes * 60));

        ((TextView)findViewById(R.id.runRecipeTitleView)).setText(RunRecipeActivity.recipe.name);
        ((TextView)findViewById(R.id.recipeTotalTimeView)).setText("Instructions, total time: "
                + minutes + " mins, " + seconds + " secs.");

        //set up the adapters
        ingredientAdapter = new IngredientAdapter(this);
        ListView listView1 = (ListView) findViewById(R.id.recipeIngredientListView);
        listView1.setAdapter(ingredientAdapter);

        timerAdapter = new TimerAdapter(this);
        ListView listView2 = (ListView) findViewById(R.id.recipeStepListView);
        listView2.setAdapter(timerAdapter);

        runningTimerAdapter = new ActiveTimerAdapter(this);
        ListView listView3 = (ListView) findViewById(R.id.runningStepsListView);
        listView3.setAdapter(runningTimerAdapter);

        /*
        Tentative steps for timers:

        Make a list for the timers. Make an adapter for the list. make the original timer (list[0]?)
        add other timers at the appropriate time (also storing an index somehow?). It could also check
        for things starting in the next ~15 seconds and add a 'timer incoming' warning? Each timer's onFinish uses the index
        to destroy itself, and notify that the data changed. use the onTick of each timer to update its progress bar.
        */

        //reset each step's counter.
        for (int i = 0; i < recipe.steps.size(); i++) {
            recipe.steps.get(i).secondsLeft = recipe.steps.get(i).length;
        }


    }

    public void playPause(View view) {
        if (!started) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //hide the recipeDisplay linear layout and show the stepsDisplay layout instead if this
            //is the first time the user presses the start/pause button.
            LinearLayout infoLayout = (LinearLayout) findViewById(R.id.recipeDisplay);
            infoLayout.setVisibility(View.GONE);
            stepsDisplay = (LinearLayout) findViewById(R.id.stepDisplay);
            stepsDisplay.setVisibility(View.VISIBLE);
            started = true;
            ((TextView)findViewById(R.id.countdownTimerView)).setText("seconds remaining: " + secondsRemaining);

            //reset all timers
            for (int i = 0; i < recipe.steps.size(); i++) {
                recipe.steps.get(i).running = false;
            }

            //int index = 0;
            //start the countdown timer
            countDown = new CountDownTimer(secondsRemaining * 1000, 1000) {
                //int index = 0;
                public void onTick(long millisUntilFinished) {
                    if (RunRecipeActivity.recipe.isActive) {
                        long minutes = millisUntilFinished / 1000 / 60;
                        long seconds = (millisUntilFinished - (minutes * 1000 * 60)) / 1000;
                        secondsRemaining -= 1;
                        ((TextView)findViewById(R.id.countdownTimerView)).setText("Time until delicious completion: "
                                + minutes + " mins, " + seconds + " sec.");

                        for (int i = 0; i < recipe.steps.size(); i++) {
                            //if the timer start is after or = to current time, start the timer & add to the list for display.
                            if ((recipe.steps.get(i).startTime <= timeElapsed) &&
                                    (!recipe.steps.get(i).running)) {
                                recipe.steps.get(i).running = true;
                                steps.add(recipe.steps.get(i));
                                CountDownTimer newTimer = create_new_step_timer(i, recipe.steps.get(i).length * 1000, 1000).start();
                                timers.add(newTimer);
                            }
                        }

                        runningTimerAdapter.notifyDataSetChanged();
                        timeElapsed += 1;
                    }
                }

                public void onFinish() {
                    ((TextView)findViewById(R.id.countdownTimerView)).setText("done!");
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }.start();
        } else {
            started = false;
            countDown.cancel();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        ((TextView)findViewById(R.id.playPause)).setText(RunRecipeActivity.recipe.isActive ? "Resume" : "Pause");
        RunRecipeActivity.recipe.isActive = !RunRecipeActivity.recipe.isActive;
    }

    private CountDownTimer create_new_step_timer(int index, int length, int interval) {
        final int ind = index;

        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).index = i;
        }

        return new CountDownTimer(length, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (RunRecipeActivity.recipe.isActive) {
                    recipe.steps.get(ind).secondsLeft -= 1;
                }
            }

            @Override
            public void onFinish() {
                for (int j = 0; j < recipe.steps.size(); ++j) {
                    if (recipe.steps.get(j).index == ind) {
                        steps.remove(j);
                        runningTimerAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        };
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
            TextView descriptionView = (TextView)arg1.findViewById(R.id.timerDescriptionView);
            TextView timeLeftView = (TextView)arg1.findViewById(R.id.timeLeftView);
            Step step = RunRecipeActivity.recipe.steps.get(arg0);

            descriptionView.setText(step.description);
            timeLeftView.setText(step.length + " seconds");

            return arg1;
        }
    }

    public class ActiveTimerAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public ActiveTimerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return steps.size();
        }
        @Override
        public Object getItem(int arg0) {
            return steps.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) RunRecipeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.active_timer_list_view, arg2, false);
            }
            TextView descriptionView = (TextView)arg1.findViewById(R.id.runningTimerDescriptionView);
            TextView timeLeftView = (TextView)arg1.findViewById(R.id.runtimeLeftView);

            ProgressBar progress = (ProgressBar)arg1.findViewById(R.id.stepProgressBar);

            Step step = steps.get(arg0);

            descriptionView.setText(step.description);
            timeLeftView.setText(step.secondsLeft + " seconds");
            progress.setProgress(50);

            return arg1;
        }
    }

    /*public class StepCountDownTimer{
        public int index;
        public CountDownTimer timer;

        public StepCountDownTimer(int index, int length, int interval) {

            this.index = index;
            this.timer = create_new_step_timer(index, length, interval);
        }
    }*/
}