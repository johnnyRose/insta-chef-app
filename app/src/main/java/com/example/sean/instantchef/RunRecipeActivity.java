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
    private boolean started;
    public LinearLayout stepsDisplay;
    public static CountDownTimer countDown;
    public int secondsRemaining;
    int timeElapsed;
    public static ArrayList<Step> steps = new ArrayList<>();
    public static ArrayList<CounterWrapper> timers = new ArrayList<>();
    public static int timerIndex;
    public static int stepIndex;
    public static boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_recipe);

        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("sean_and_john.run_recipe.info");
        RunRecipeActivity.recipe = MainActivity.recipes.get((int) id);
        secondsRemaining = RunRecipeActivity.recipe.totalRunTimeSeconds;
        long minutes = secondsRemaining / 60;
        long seconds = (secondsRemaining - (minutes * 60));
        done = false;
        RunRecipeActivity.stepIndex = 0;
        RunRecipeActivity.timerIndex = 0;
        timeElapsed = 0;
        started = false;

        ((TextView)findViewById(R.id.runRecipeTitleView)).setText(RunRecipeActivity.recipe.name);
        ((TextView)findViewById(R.id.recipeTotalTimeView)).setText("Instructions, total time: "
                + minutes + " mins, " + seconds + " secs.");

        //reset all the lists in case this isn't the first time we've run it.
        for (int i = 0; i < recipe.steps.size(); i++) {
            recipe.steps.get(i).secondsLeft = recipe.steps.get(i).length;
            recipe.steps.get(i).percentDone = 0;
        }
        for (int i = 0; i < steps.size(); i++) {
            steps.remove(0);
        }

        timers = new ArrayList<>();

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
    }

    public void playPause(View view) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //hide the recipeDisplay linear layout and show the stepsDisplay layout instead if this
        //is the first time the user presses the start/pause button.
        LinearLayout infoLayout = (LinearLayout) findViewById(R.id.recipeDisplay);
        infoLayout.setVisibility(View.GONE);
        stepsDisplay = (LinearLayout) findViewById(R.id.stepDisplay);
        stepsDisplay.setVisibility(View.VISIBLE);
        started = true;
        ((TextView) findViewById(R.id.countdownTimerView)).setText("seconds remaining: " + secondsRemaining);

        //reset all timers
        for (int i = 0; i < recipe.steps.size(); i++) {
            recipe.steps.get(i).running = false;
        }

        //start the countdown timer
        countDown = new CountDownTimer(secondsRemaining * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (RunRecipeActivity.recipe.isActive) {
                    long minutes = millisUntilFinished / 1000 / 60;
                    long seconds = (millisUntilFinished - (minutes * 1000 * 60)) / 1000;
                    seconds++;
                    secondsRemaining -= 1;
                    ((TextView) findViewById(R.id.countdownTimerView)).setText("Time until delicious completion: "
                            + minutes + " mins, " + seconds + " sec.");

                    for (int i = 0; i < recipe.steps.size(); i++) {
                        //if the timer start is after or = to current time, start the timer & add to the list for display.
                        if ((recipe.steps.get(i).startTime <= timeElapsed) &&
                                (!recipe.steps.get(i).running)) {
                            recipe.steps.get(i).running = true;
                            steps.add(recipe.steps.get(i));
                            CountDownTimer newTimer = create_new_step_timer(RunRecipeActivity.timerIndex, recipe.steps.get(i).length * 1000, 1000).start();
                            timers.add(new CounterWrapper(RunRecipeActivity.stepIndex, RunRecipeActivity.timerIndex, newTimer));
                            RunRecipeActivity.timerIndex++;
                            RunRecipeActivity.stepIndex++;
                        }
                    }
                    runningTimerAdapter.notifyDataSetChanged();
                    timeElapsed += 1;
                }
            }

            public void onFinish() {
                done = true;
            }
        }.start();

        findViewById(R.id.playPause).setVisibility(View.GONE);
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
                int stepIndex = timers.get(ind).stepIndex;
                steps.get(stepIndex).secondsLeft -= 1;

                //update the progressbar percentage.
                timers.get(ind).timeElapsed += 1;
                steps.get(stepIndex).percentDone = (int) (((float) timers.get(ind).timeElapsed / steps.get(stepIndex).length) * 100);
            }

            @Override
            public void onFinish() {

                //decrement index so that if there are timers added after this one
                //expires, they'll have a correct index.
                RunRecipeActivity.stepIndex--;
                steps.remove(timers.get(ind).stepIndex);

                //decrement all the step indices so when we delete the expiring
                //step, everything else points to its proper step.
                for (int i = ind; i < timers.size(); i++) {
                    timers.get(i).stepIndex--;
                }

                //if 'done' is true, it means the main timer finished and we need to clean up.
                if (done) {
                    for (int i = 0; i < steps.size(); i++) {
                        steps.get(i).timer.cancel();
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    findViewById(R.id.playPause).setVisibility(View.VISIBLE);
                    RunRecipeActivity.recipe.isActive = false;
                    ((TextView)findViewById(R.id.countdownTimerView)).setText("done!");
                    finish();
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
            progress.setProgress(step.percentDone);

            return arg1;
        }
    }

    public class CounterWrapper {
        public int stepIndex;
        public int timerIndex;
        public int timeElapsed;
        public CountDownTimer timer;

        public CounterWrapper (int stepIndex, int timerIndex, CountDownTimer timer) {
            this.stepIndex = stepIndex;
            this.timerIndex = timerIndex;
            this.timer = timer;
            this.timeElapsed = 0;
        }
    }
}