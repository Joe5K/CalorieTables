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
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//Aplikacia na evidovanie prijateho jedla a kalorii, ukadanie do db, export v JSON/XML, ukladanie geolokacie a casu pridania jedla, pridavanie obrazkov, ukladanie nastaveni do pamäte
//
public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<Food> loadedFoods;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                System.out.println("kk");
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

        CalendarView calendarView = findViewById(R.id.calendarView);
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
                CalendarView calendarView = findViewById(R.id.calendarView);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(new Date(calendarView.getDate()));

                //Snackbar.make(view, selectedDate, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                addFood(calendarView.getDate());
            }
        });

        loadDataToCalendar(calendarView);
    }

    public void loadDataToCalendar(CalendarView calendarView){
        long time = getMidnightTime(calendarView.getDate());

        loadedFoods = dbHelper.getFoodsInDay(time);

        ArrayAdapter adapter = new ArrayAdapter<Food>(this,
                android.R.layout.simple_list_item_1, loadedFoods);

        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = (Food) listView.getItemAtPosition(i);
                editFood(food.getId());
            }
        });
    }

    public void editFood(int id){
        Intent intent = new Intent(this, EditFoodDataActivity.class);

        CalendarView calendarView = findViewById(R.id.calendarView);

        intent.putExtra("ACTION", "EDIT");
        intent.putExtra("ID", id);
        intent.putExtra("DATE", getMidnightTime(calendarView.getDate()));
        startActivity(intent);
    }

    public void addFood(long date) {
        Intent intent = new Intent(this, EditFoodDataActivity.class);

        intent.putExtra("ACTION", "NEW");
        intent.putExtra("DATE", getMidnightTime(date));
        startActivity(intent);
    }

    private static long getMidnightTime(long time){
        Calendar rightNow = Calendar.getInstance();

        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);

        long sinceMidnight = (time + offset) %
                (24 * 60 * 60 * 1000);
        return time - sinceMidnight;
    }
}