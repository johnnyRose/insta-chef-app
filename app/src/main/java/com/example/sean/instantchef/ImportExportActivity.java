package com.example.sean.instantchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

        (findViewById(R.id.importProgressBar)).setVisibility(View.VISIBLE);
        //TODO: implement file chooser, maybe.
        //apparently android API has no built-in support for a file chooser. lame.

        //we'll use a hardcoded JSON string for now to work out the parsing, see the bottom of this file for the pretty version.
        String json_string = "{\"recipe0\":{\"id\":0,\"description\":\"A simple recipe to test functionality.\",\"name\":\"Sean's Awesome Egg Sammich\",\"dateCreated\":\"\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"Eggs, whole.\",\"amount\":\"2\"},\"ingredient1\":{\"id\":1,\"description\":\"Bacon\",\"amount\":\"4 pieces\"},\"ingredient2\":{\"id\":2,\"description\":\"Bread\",\"amount\":\"2 slices\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"Using a skillet, cook the pieces of bacon over medium heat until slightly crispy. Set aside.\",\"startTime\":0,\"length\":300},\"step1\":{\"id\":1,\"description\":\"Turn the burner down to medium-low and fry the 2 eggs until over-medium (or desired consistency).\",\"startTime\":300,\"length\":180},\"step2\":{\"id\":2,\"description\":\"Toast your bread, butter while still warm.\",\"startTime\":330,\"length\":180},\"step3\":{\"id\":3,\"description\":\"Assemble your creation.\",\"startTime\":480,\"length\":60}}},\"recipe1\":{\"id\":1,\"description\":\"so good.\",\"name\":\"Grilled Cheese\",\"dateCreated\":\"\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"Cheese slices.\",\"amount\":\"2\"},\"ingredient1\":{\"id\":1,\"description\":\"Bread\",\"amount\":\"2\"},\"ingredient2\":{\"id\":2,\"description\":\"Mayo\",\"amount\":\"\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"Pre-heat the skillet on medium. Coat 1 side of each piece of bread with mayo, put the cheese slices inside.\",\"startTime\":0,\"length\":80},\"step1\":{\"id\":1,\"description\":\"Fry until golden-brown, mayo side out.\",\"startTime\":80,\"length\":120}}},\"recipe2\":{\"id\":2,\"description\":\"so good.\",\"name\":\"Test Recipe\",\"dateCreated\":\"\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"Cheese slices.\",\"amount\":\"2\"},\"ingredient1\":{\"id\":1,\"description\":\"Bread\",\"amount\":\"2\"},\"ingredient2\":{\"id\":2,\"description\":\"Mayo\",\"amount\":\"\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"Do stuff\",\"startTime\":0,\"length\":10},\"step1\":{\"id\":1,\"description\":\"Do different stuff.\",\"startTime\":5,\"length\":25},\"step2\":{\"id\":2,\"description\":\"Just wing it, idk.\",\"startTime\":12,\"length\":15},\"step3\":{\"id\":3,\"description\":\"Random chopping with a knife.\",\"startTime\":2,\"length\":5}}},\"recipe3\":{\"id\":3,\"description\":\"\",\"name\":\"Test 2\",\"dateCreated\":\"Mon May 11 21:49:58 CDT 2015\",\"ingredients\":{\"ingredient0\":{\"id\":0,\"description\":\"thing\",\"amount\":\"\"}},\"steps\":{\"step0\":{\"id\":0,\"description\":\"stuff\",\"startTime\":0,\"length\":3},\"step1\":{\"id\":0,\"description\":\"more stuff\",\"startTime\":5,\"length\":5}}}}";
        boolean duplicates = Recipe.importRecipes(json_string);
        if (duplicates) Toast.makeText(this, "Duplicate recipes were not imported.", Toast.LENGTH_LONG).show();
        (findViewById(R.id.importProgressBar)).setVisibility(View.INVISIBLE);

        //go to the main screen after import.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void export_file(View view) {

        (findViewById(R.id.exportProgressBar)).setVisibility(View.VISIBLE);
        //TODO: see above.
        String export_string = "";
        for (int i = 0; i < MainActivity.recipes.size(); i++){
            export_string += "\"recipe" + i + "\": " + MainActivity.recipes.get(i).serialize();
            if (i < MainActivity.recipes.size() - 1) {
                export_string += ", "; //add a comma after every recipe except the last.
            }
        }
        Log.d("instant-chef-app", "{" + export_string + "}");
        (findViewById(R.id.exportProgressBar)).setVisibility(View.INVISIBLE);
    }
}

/* A copy of the above-minified JSON string.

{
    "recipe0": {
        "id": 0,
        "description": "A simple recipe to test functionality.",
        "name": "Sean's Awesome Egg Sammich",
        "dateCreated": "",
        "ingredients": {
            "ingredient0": {
                "id": 0,
                "description": "Eggs, whole.",
                "amount": "2"
            },
            "ingredient1": {
                "id": 1,
                "description": "Bacon",
                "amount": "4 pieces"
            },
            "ingredient2": {
                "id": 2,
                "description": "Bread",
                "amount": "2 slices"
            }
        },
        "steps": {
            "step0": {
                "id": 0,
                "description": "Using a skillet, cook the pieces of bacon over medium heat until slightly crispy. Set aside.",
                "startTime": 0,
                "length": 300
            },
            "step1": {
                "id": 1,
                "description": "Turn the burner down to medium-low and fry the 2 eggs until over-medium (or desired consistency).",
                "startTime": 300,
                "length": 180
            },
            "step2": {
                "id": 2,
                "description": "Toast your bread, butter while still warm.",
                "startTime": 330,
                "length": 180
            },
            "step3": {
                "id": 3,
                "description": "Assemble your creation.",
                "startTime": 480,
                "length": 60
            }
        }
    },
    "recipe1": {
        "id": 0,
        "description": "so good.",
        "name": "Grilled Cheese",
        "dateCreated": "",
        "ingredients": {
            "ingredient0": {
                "id": 0,
                "description": "Cheese slices.",
                "amount": "2"
            },
            "ingredient1": {
                "id": 1,
                "description": "Bread",
                "amount": "2"
            },
            "ingredient2": {
                "id": 2,
                "description": "Mayo",
                "amount": ""
            }
        },
        "steps": {
            "step0": {
                "id": 0,
                "description": "Pre-heat the skillet on medium. Coat 1 side of each piece of bread with mayo, put the cheese slices inside.",
                "startTime": 0,
                "length": 80
            },
            "step1": {
                "id": 1,
                "description": "Fry until golden-brown, mayo side out.",
                "startTime": 80,
                "length": 120
            }
        }
    },
    "recipe2": {
        "id": 0,
        "description": "so good.",
        "name": "Test Recipe",
        "dateCreated": "",
        "ingredients": {
            "ingredient0": {
                "id": 0,
                "description": "Cheese slices.",
                "amount": "2"
            },
            "ingredient1": {
                "id": 1,
                "description": "Bread",
                "amount": "2"
            },
            "ingredient2": {
                "id": 2,
                "description": "Mayo",
                "amount": ""
            }
        },
        "steps": {
            "step0": {
                "id": 0,
                "description": "Do stuff",
                "startTime": 0,
                "length": 10
            },
            "step1": {
                "id": 1,
                "description": "Do different stuff.",
                "startTime": 5,
                "length": 25
            },
            "step2": {
                "id": 2,
                "description": "Just wing it, idk.",
                "startTime": 12,
                "length": 15
            },
            "step3": {
                "id": 3,
                "description": "Random chopping with a knife.",
                "startTime": 2,
                "length": 5
            }
        }
    }
}

// */