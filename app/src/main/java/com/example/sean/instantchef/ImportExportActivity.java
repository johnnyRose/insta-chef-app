package com.example.sean.instantchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
        String json_string = "{\"recipe0\":{\"id\":0,\"description\":\"test recipe\",\"name\":\"test recipe3\",\"dateCreated\":\"\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"pizza\",\"amount\":\"one\"},\"ingredient1\":{\"id\":1,\"description\":\"pepperoni\",\"amount\":\"16\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"boil\",\"startTime\":0,\"length\":40},\"step1\":{\"id\":1,\"description\":\"boil more\",\"startTime\":30,\"length\":40}}},\"recipe1\":{\"id\":0,\"description\":\"test recipe\",\"name\":\"test recipe4\",\"dateCreated\":\"\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"pizza\",\"amount\":\"one\"},\"ingredient1\":{\"id\":1,\"description\":\"pepperoni\",\"amount\":\"16\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"boil\",\"startTime\":0,\"length\":40},\"step1\":{\"id\":1,\"description\":\"boil more\",\"startTime\":30,\"length\":40}}}}";
        Recipe.importRecipes(json_string);

        //go to the main screen after import.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void export_file(View view) {
        //TODO: see above.
        String export_string = "";
        for (int i = 0; i < MainActivity.recipes.size(); i++){
            export_string += "\"recipe" + i + "\": " + MainActivity.recipes.get(i).serialize();
            if (i < MainActivity.recipes.size() - 1) {
                export_string += ", "; //add a comma after every recipe except the last.
            }
        }
        Log.d("instant-chef-app", "{" + export_string + "}");
    }
}

/* A copy of the above-minified JSON string.

{
    "recipe0": {
        "id": 0,
        "description": "test recipe",
        "name": "test recipe3",
        "dateCreated": "",
        "ingredients": {
            "ingredient0": {
                "id": 0,
                "description": "pizza",
                "amount": "one"
            },
            "ingredient1": {
                "id": 1,
                "description": "pepperoni",
                "amount": "16"
            }
        },
        "steps": {
            "step0": {
                "id": 0,
                "description": "boil",
                "startTime": 0,
                "length": 40
            },
            "step1": {
                "id": 1,
                "description": "boil more",
                "startTime": 30,
                "length": 40
            }
        }
    },
    "recipe1": {
        "id": 0,
        "description": "test recipe",
        "name": "test recipe4",
        "dateCreated": "",
        "ingredients": {
            "ingredient0": {
                "id": 0,
                "description": "pizza",
                "amount": "one"
            },
            "ingredient1": {
                "id": 1,
                "description": "pepperoni",
                "amount": "16"
            }
        },
        "steps": {
            "step0": {
                "id": 0,
                "description": "boil",
                "startTime": 0,
                "length": 40
            },
            "step1": {
                "id": 1,
                "description": "boil more",
                "startTime": 30,
                "length": 40
            }
        }
    }
}

// */