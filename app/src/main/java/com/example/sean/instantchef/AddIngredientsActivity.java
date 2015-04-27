package com.example.sean.instantchef;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sean on 4/26/2015.
 */
public class AddIngredientsActivity extends ActionBarActivity {
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        ingredientAdapter = new IngredientAdapter(this);
        ListView listView = (ListView) findViewById(R.id.ingredientScreenListView);
        listView.setAdapter(ingredientAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: decide what behavior we want when the user clicks an ingredient.
            }
        });
    }

    public void save_ingredient(View view) {
        TextView desc = (TextView)findViewById(R.id.ingredientDescriptionEdit);
        TextView amt = (TextView)findViewById(R.id.ingredientAmountEdit);
        if (desc.getText().equals("")) {
            Toast.makeText(this, "Not enough information to save the ingredient. You must"
                    + " have at least a description.", Toast.LENGTH_LONG).show();
        } else {
            Ingredient ingredient = new Ingredient(0, desc.getText().toString(), amt.getText().toString());
            CreateEditActivity.ingredients.add(ingredient);
            ingredientAdapter.notifyDataSetChanged();
            desc.setText("");
            amt.setText("");
        }
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

    public class IngredientAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public IngredientAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return CreateEditActivity.ingredients.size();
        }
        @Override
        public Object getItem(int arg0) {
            return CreateEditActivity.ingredients.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) AddIngredientsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.ingredient_list_view, arg2, false);
            }
            TextView nameView = (TextView)arg1.findViewById(R.id.nameView);
            TextView amountView = (TextView)arg1.findViewById(R.id.amountView);

            Ingredient ingredient = CreateEditActivity.ingredients.get(arg0);
            nameView.setText(ingredient.description);
            amountView.setText(ingredient.amount);

            return arg1;
        }
    }
}
