package com.example.sean.instantchef;

import android.os.AsyncTask;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by John Rosewicz on 4/7/2015.
 */

public class Timer extends AsyncTask implements Serializable {

    public int id; // database integration
    public String startDescription; // short description to be shown when timer starts, "add veggies", "reduce heat to simmer"
    public String endDescription; // short description to be shown when timer ends - startDescription and endDescription may not always have values
    public int secondsRemaining; // duration of timer in seconds
    public int startWhenParentTime; // if not primary timer, at what point in parent's life should this timer begin?
    public ArrayList<Timer> timers; // list of timers to check for their startWhenParentTime
    public Recipe recipe; // pointer back to recipe

    public Timer() {
        this.timers = new ArrayList<Timer>();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected void onPreExecute() {
        if (this.startDescription.length() > 0) {
            // show toast or something to display startDescription
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        while (this.secondsRemaining > 0) {
            if (this.recipe.isActive) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                --this.secondsRemaining;

                for (int i = 0; i < this.timers.size(); ++i) {
                    if (this.timers.get(i).startWhenParentTime == this.secondsRemaining) {
                        this.timers.get(i).execute();
                    }
                }
            } else { // otherwise, the recipe has been paused
                try {
                    Thread.sleep(200); // chill out for a bit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        if (this.endDescription.length() > 0) {
            // show toast or something to display endDescription
        }
    }

    public String serialize() {
        // see note in Timer.serialize()
        return "";
    }
}
