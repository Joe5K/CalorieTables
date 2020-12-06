package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditFoodDataActivity extends AppCompatActivity {
    DBHelper dbHelper;
    long dateAtMidnight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        dateAtMidnight = intent.getLongExtra("DATE", 0);
        dbHelper = new DBHelper(this);
        if (Objects.equals(intent.getStringExtra("ACTION"), "NEW")){
            loadNewScreen();
        }
        else if (Objects.equals(intent.getStringExtra("ACTION"), "EDIT")){
            int id = intent.getIntExtra("ID", 0);
            loadEditScreen(id);
        }
    }

    private void loadNewScreen (){
        setContentView(R.layout.activity_new_food);

        Button newButton = findViewById(R.id.buttonNew);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEditScreen(0);
            }
        });
    }

    private void loadEditScreen (final int id){
        setContentView(R.layout.activity_edit_food_data);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        if (id != 0){
            Food food = dbHelper.getFood(id);
            ((EditText) findViewById(R.id.name)).setText(food.getName());
            ((EditText) findViewById(R.id.weight)).setText(getStringFromDouble(food.getWeight()));
            ((EditText) findViewById(R.id.calories)).setText(getStringFromDouble(food.getCalories()));
            ((EditText) findViewById(R.id.fats)).setText(getStringFromDouble(food.getFats()));
            ((EditText) findViewById(R.id.saccharides)).setText(getStringFromDouble(food.getSaccharides()));
            ((EditText) findViewById(R.id.sugars)).setText(getStringFromDouble(food.getSugars()));
            ((EditText) findViewById(R.id.proteins)).setText(getStringFromDouble(food.getProteins()));
            ((EditText) findViewById(R.id.salt)).setText(getStringFromDouble(food.getSalt()));
            Pair<Integer, Integer> time = getTimeFromDate(food.getDate());
            timePicker.setHour(time.first);
            timePicker.setMinute(time.second);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker = findViewById(R.id.timePicker);
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                double weight = getDoubleFromEditText(R.id.weight);
                double calories = getDoubleFromEditText(R.id.calories);
                double fats = getDoubleFromEditText(R.id.fats);
                double saccharides = getDoubleFromEditText(R.id.saccharides);
                double sugars = getDoubleFromEditText(R.id.sugars);
                double proteins = getDoubleFromEditText(R.id.proteins);
                double salt = getDoubleFromEditText(R.id.salt);
                long time = dateAtMidnight + timePicker.getMinute()*60000 + timePicker.getHour()*3600000;

                Food food = new Food(name, weight, calories, fats, saccharides, sugars, proteins, salt, time);

                if (id == 0) {
                    dbHelper.addFood(food);
                }
                else {
                    food.setId(id);
                    dbHelper.editFood(food);
                }

                finish();
            }
        });
    }

    private double getDoubleFromEditText(int id){
        try{
            double value = Double.parseDouble(((EditText) findViewById(id)).getText().toString());
            return value < 0 ? 0 : value;
        }
        catch (Exception e){
            return 0;
        }
    }

    private static String getStringFromDouble(double d){
        if(d == (long) d)
            return String.format(Locale.getDefault(), "%d",(long) d);
        else
            return String.format("%s",d);
    }

    private static Pair<Integer, Integer> getTimeFromDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String selectedDate = sdf.format(new Date(time));
        return new Pair<>(Integer.parseInt(selectedDate.substring(0,2)), Integer.parseInt(selectedDate.substring(3,5)));
    }
}