package com.example.sean.instantchef;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by John Rosewicz on 4/8/2015.
 */
public class Recipe implements Serializable {

    public int id; // database integration
    public String description; // description of recipe
    public String imagePath; // path to image on web
    public String name; // recipe name
    public Date dateCreated; // date created
    public String createdBy; // author
    public Timer primaryTimer; // main recipe timer
    public boolean isActive; // pause/resume timer execution

    public Recipe() {
        this.dateCreated = new Date(); // current date and time
        this.primaryTimer = new Timer();
        this.primaryTimer.setRecipe(this);
        this.isActive = false;
    }

    public void start() {
        this.isActive = true;
        this.primaryTimer.execute();
    }

    public void togglePause() {
        this.isActive = !this.isActive;
    }

    public void addTimer(Timer parentTimer, Timer newTimer) {
        newTimer.setRecipe(this);
        parentTimer.timers.add(newTimer);
    }

    public void removeTimer(Timer parentTimer, Timer targetTimer) {
        parentTimer.timers.remove(targetTimer);
    }

    public String serialize() {
        /*

        haven't quite figured this out. to avoid circular serialization however,
        before serialization we will need to set each timer's recipe to null
        just before serialization occurs. when we de-serialize, every timer's
        recipe will need to be set to the main recipe. i think there is a cleaner
        way to do this, but it isn't inherently obvious to me right now.



        problem: when Recipe.isActive is false, all timers need to pause. currently,
        this is implemented by checking the recipe's isActive boolean before every
        loop, while the timer's value is still greater than 0. however, having a
        recipe on timers and timers on recipes will make it impossible to serialize.

        solution: use a database to store recipes, with a single foreign key to timer.ID.
        each timer contains a recursive one-to-many FK to timer.ID. each timer will also
        contain a FK of the recipe.ID.

        i wonder if android studio has a package equivalent to Entity Framework.

        */
        return "";
    }
}
