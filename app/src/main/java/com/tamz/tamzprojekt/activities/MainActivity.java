package com.tamz.tamzprojekt.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tamz.tamzprojekt.FoodsArrayAdapter;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private ArrayList<Food> loadedFoods;
    private boolean deleting = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (!deleting){
                    Snackbar.make(findViewById(R.id.action_delete), "Delete mod is on. Click on the food you wish to delete", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    item.setIcon(R.drawable.ic_baseline_delete_forever_24);
                    deleting = true;
                }
                else {
                    Snackbar.make(findViewById(R.id.action_delete), "Delete mod is off.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    item.setIcon(R.drawable.ic_baseline_delete_outline_24);
                    deleting = false;
                }
                return true;
            case R.id.action_stats:
                Intent intent = new Intent(this, Statistics.class);
                startActivityForResult(intent, 1);
                return true;

            case R.id.action_settings:
                Intent intent2 = new Intent(this, Settings.class);
                startActivityForResult(intent2, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        final CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                calendarView.setDate(c.getTimeInMillis());

                loadDataToCalendar(calendarView);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(new Date(calendarView.getDate()));

                addFood(calendarView.getDate());
            }
        });

        final ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = (Food) listView.getItemAtPosition(i);
                if(deleting){
                    dbHelper.deleteFood(food);
                    reload();
                }
                else
                    editFood(food.getId());
            }
        });

        loadDataToCalendar(calendarView);
    }

    public void loadDataToCalendar(CalendarView calendarView){
        long time = getMidnightTime(calendarView.getDate());

        loadedFoods = dbHelper.getFoodsInDay(time);

        FoodsArrayAdapter adapter = new FoodsArrayAdapter(this, loadedFoods);

        /*ArrayAdapter adapter = new ArrayAdapter<Food>(this,
                android.R.layout.simple_list_item_1, loadedFoods);*/

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void editFood(int id){
        Intent intent = new Intent(this, EditFoodDataActivity.class);

        CalendarView calendarView = findViewById(R.id.calendarView);

        intent.putExtra("ACTION", "EDIT");
        intent.putExtra("ID", id);
        intent.putExtra("DATE", getMidnightTime(calendarView.getDate()));
        startActivityForResult(intent, 1);
    }

    public void addFood(long date) {
        Intent intent = new Intent(this, EditFoodDataActivity.class);

        intent.putExtra("ACTION", "NEW");
        intent.putExtra("DATE", getMidnightTime(date));
        startActivityForResult(intent,1);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        super.onActivityResult(requestCode, resultCode, data);
        reload();
    }

    private static long getMidnightTime(long time){
        Calendar rightNow = Calendar.getInstance();

        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);

        long sinceMidnight = (time + offset) %
                (24 * 60 * 60 * 1000);
        return time - sinceMidnight;
    }

    public void reload() {
        loadDataToCalendar((CalendarView) findViewById(R.id.calendarView));
    }
}

/*TODO
<com.google.android.material.tabs.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:tabMode="fixed">

        <com.google.android.material.tabs.TabItem
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="a" />

        <com.google.android.material.tabs.TabItem
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="b" />

        <com.google.android.material.tabs.TabItem
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="c" />

    </com.google.android.material.tabs.TabLayout>*/