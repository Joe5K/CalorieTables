package com.tamz.tamzprojekt.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.Common;
import com.tamz.tamzprojekt.FoodsArrayAdapter;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.util.ArrayList;
import java.util.Calendar;

//Aplikacia na evidovanie prijateho jedla a kalorii, ukadanie do db, export v JSON/XML, ukladanie geolokacie a casu pridania jedla, pridavanie obrazkov, ukladanie nastaveni do pamäte

//UNDONE - export v JSON/XML, ukladanie geolokacie pridania jedla, pridavanie obrazkov

/*Advanced GUI - Lists, Tabs, Fragments, Gestures
//Database – SQLite
//Multimedia – Audio, Video, playback, recording
//Signal Processing – Image, Sound, Sensors
//Persistent Storage – SharedPreferences
Networking – Downloading data, JSON, WS
 */

//statistiky a grafy, search, export JSON


public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    CalendarView calendarView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Intent intent = new Intent(this, StatisticsActivity.class);
                intent.putExtra("DATE", calendarView.getDate());
                startActivityForResult(intent, 1);
                return true;

            case R.id.action_settings:
                Intent intent2 = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent2, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        super.onActivityResult(requestCode, resultCode, data);
        reload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                calendarView.setDate(c.getTimeInMillis());

                loadDataToCalendar();

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFood();
            }
        });

        final ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = (Food) listView.getItemAtPosition(i);

                editFood(food);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = (Food) listView.getItemAtPosition(i);

                dbHelper.deleteFood(food);
                reload();

                return true;
            }
        });

        loadDataToCalendar();

        Toast.makeText(getApplicationContext(), "Hold the food record to delete it.", Toast.LENGTH_LONG).show();
    }

    public void loadDataToCalendar(){
        long time = Common.getMidnightTime(calendarView.getDate());

        ArrayList<Food> loadedFoods = dbHelper.getFoodsInDay(time);

        FoodsArrayAdapter adapter = new FoodsArrayAdapter(this, loadedFoods);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void editFood(Food food){
        Intent intent = new Intent(this, EditFoodDataActivity.class);
        intent.putExtra("ID", food.getId());
        intent.putExtra("DATE", Common.getMidnightTime(calendarView.getDate()));
        intent.putExtra("ACTION", "EDIT");
        startActivityForResult(intent, 1);
    }

    public void addFood() {
        Intent intent = new Intent(this, NewFoodActivity.class);
        intent.putExtra("DATE", Common.getMidnightTime(calendarView.getDate()));

        startActivityForResult(intent,1);
    }

    public void reload() {
        loadDataToCalendar();
    }
}