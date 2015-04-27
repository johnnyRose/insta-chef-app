package com.example.sean.instantchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sean on 4/9/2015.
 */
public class ImportExportActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
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

    public void import_file(View view) throws JSONException {
        //TODO: implement file chooser, maybe.
        //apparently android API has no built-in support for a file chooser. lame.

        //we'll use a hardcoded JSON string for now to work out the parsing, see the bottom of this file for the pretty version.
        String json_string = "{\"recipe0\":{\"id\":0,\"description\":\"test recipe\",\"imagePath\":\"/dev/null/\",\"name\":\"Test Recipe\",\"dateCreated\":\"4/10/2015\",\"createdBy\":\"Sean Porter\",\"isActive\":false,\"ingredients\":{\"id\":0,\"description\":\"carrots\"},\"primaryTimer\":{\"id\":0,\"startDescription\":\"add veggies\",\"endDescription\":\"\",\"secondsRemaining\":30,\"startWhenParentTime\":30,\"timers\":{\"id\":1,\"startDescription\":\"raise heat to boil\",\"endDescription\":\"\",\"secondsRemaining\":30,\"startWhenParentTime\":30,\"timers\":{}}}},\"recipe1\":{\"id\":1,\"description\":\"test recipe 2\",\"imagePath\":\"/dev/null/\",\"name\":\"Test Recipe 2\",\"dateCreated\":\"4/10/2015\",\"createdBy\":\"Sean Porter\",\"isActive\":false,\"ingredients\":{\"id\":0,\"description\":\"carrots\"},\"primaryTimer\":{\"id\":0,\"startDescription\":\"add veggies\",\"endDescription\":\"\",\"secondsRemaining\":30,\"startWhenParentTime\":30,\"timers\":{\"id\":1,\"startDescription\":\"raise heat to boil\",\"endDescription\":\"\",\"secondsRemaining\":30,\"startWhenParentTime\":30,\"timers\":{}}}}}";
        JSONObject json_file = new JSONObject(json_string);

        for (int i = 0; i < json_file.length(); i++) {
            JSONObject current_recipe = json_file.getJSONObject("recipe" + i);
            Recipe recipe = new Recipe(current_recipe.getString("description"), current_recipe.getString("name"),
                    current_recipe.getString("dateCreated"), current_recipe.getString("createdBy"));
            //TODO: add recipe to database here.
            MainActivity.recipes.add(recipe);

            //go to the main screen after import.
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void export_file(View view) {
        //TODO: see above.
    }
}

/* A copy of the above-minified JSON string.

{
    "recipe0": {
        "id": 0,
        "description": "test recipe",
        "imagePath": "/dev/null/",
        "name": "Test Recipe",
        "dateCreated": "4/10/2015",
        "createdBy": "Sean Porter",
        "isActive": false,
        "ingredients": {
            "id": 0,
            "description": "carrots"
        },
        "primaryTimer": {
            "id": 0,
            "startDescription": "add veggies",
            "endDescription": "",
            "secondsRemaining": 30,
            "startWhenParentTime": 30,
            "timers": {
                "id": 1,
                "startDescription": "raise heat to boil",
                "endDescription": "",
                "secondsRemaining": 30,
                "startWhenParentTime": 30,
                "timers": {}
            }
        }
    },
    "recipe1": {
        "id": 1,
        "description": "test recipe 2",
        "imagePath": "/dev/null/",
        "name": "Test Recipe 2",
        "dateCreated": "4/10/2015",
        "createdBy": "Sean Porter",
        "isActive": false,
        "ingredients": {
            "id": 0,
            "description": "carrots"
        },
        "primaryTimer": {
            "id": 0,
            "startDescription": "add veggies",
            "endDescription": "",
            "secondsRemaining": 30,
            "startWhenParentTime": 30,
            "timers": {
                "id": 1,
                "startDescription": "raise heat to boil",
                "endDescription": "",
                "secondsRemaining": 30,
                "startWhenParentTime": 30,
                "timers": {}
            }
        }
    }
}

// */