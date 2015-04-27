package com.example.sean.instantchef;

import android.os.AsyncTask;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by John Rosewicz on 4/7/2015.
 */

public class Step implements Serializable {

    public int id; // database integration
    public String description; // short description to be shown when timer starts, "add veggies", "reduce heat to simmer"
    public int startTime; // if not primary timer, at what point in parent's life should this timer begin?
    public int length;

    public String serialize() {
        // see note in Timer.serialize()
        return "";
    }
}
