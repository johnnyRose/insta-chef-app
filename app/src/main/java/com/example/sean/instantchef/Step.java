package com.example.sean.instantchef;

import android.os.AsyncTask;
import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by John Rosewicz on 4/7/2015.
 */

public class Step implements Serializable {

    public int id; // database integration
    public String description; // short description to be shown when timer starts, "add veggies", "reduce heat to simmer"
    public int startTime; // At what point in the recipe's life should this timer begin?
    public int length;
    public boolean running;
    public int secondsLeft;
    public int index;
    public CountDownTimer timer;
    public int percentDone;

    public Step(int id, String description, int startTime, int length) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.length = length;
        this.running = false;
    }

    public Step() {

    }

    public String serialize() {
        return "{\"id\":" + this.id + ", \"description\":\"" + this.description +  "\", \"startTime\":"
                + this.startTime +  ", \"length\":" + this.length +"}";
    }
}
