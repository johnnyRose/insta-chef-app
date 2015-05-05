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

import org.w3c.dom.Text;

import static java.lang.Integer.parseInt;

/**
 * Created by sean on 4/26/2015.
 */
public class AddTimersActivity extends ActionBarActivity {
    private TimerAdapter timerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timers);

        timerAdapter = new TimerAdapter(this);
        ListView listView = (ListView) findViewById(R.id.timerScreenListView);
        listView.setAdapter(timerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: decide what behavior we want when the user clicks a step.
                CreateEditActivity.steps.remove(position);
                timerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void save_timer(View view) {
        TextView desc = (TextView)findViewById(R.id.timerDescriptionEdit);
        TextView start = (TextView)findViewById(R.id.timerStartEdit);
        TextView length = (TextView)findViewById(R.id.timerLengthEdit);

        if (desc.getText().toString().equals("") || start.getText().toString().equals("") || length.getText().toString().equals("")) {
            Toast.makeText(this, "Not enough information to save the step. You must"
                    + " have a description, a start time and a length.", Toast.LENGTH_LONG).show();
        } else {
            Step step = new Step();
            step.description = desc.getText().toString();
            step.startTime = parseInt(start.getText().toString());
            step.length = parseInt(length.getText().toString());

            CreateEditActivity.steps.add(step);
            timerAdapter.notifyDataSetChanged();

            desc.setText("");
            start.setText("");
            length.setText("");
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

    public class TimerAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Context context;

        public TimerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return CreateEditActivity.steps.size();
        }
        @Override
        public Object getItem(int arg0) {
            return CreateEditActivity.steps.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) AddTimersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.timer_list_view, arg2, false);
            }
            TextView descriptionView = (TextView)arg1.findViewById(R.id.descriptionView);
            TextView timeLeftView = (TextView)arg1.findViewById(R.id.timeLeftView);
            Step step = CreateEditActivity.steps.get(arg0);

            descriptionView.setText(step.description);
            timeLeftView.setText(step.length + " seconds");

            return arg1;
        }
    }
}
