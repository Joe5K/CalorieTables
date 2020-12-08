package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.DataManipulator;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.util.Objects;

public class EditFoodDataActivity extends AppCompatActivity {
    DBHelper dbHelper;
    long dateAtMidnight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            ((EditText) findViewById(R.id.weight)).setText(DataManipulator.getStringFromDouble(food.getWeight()));
            ((EditText) findViewById(R.id.calories)).setText(DataManipulator.getStringFromDouble(food.getCalories()));
            ((EditText) findViewById(R.id.fats)).setText(DataManipulator.getStringFromDouble(food.getFats()));
            ((EditText) findViewById(R.id.saccharides)).setText(DataManipulator.getStringFromDouble(food.getSaccharides()));
            ((EditText) findViewById(R.id.sugars)).setText(DataManipulator.getStringFromDouble(food.getSugars()));
            ((EditText) findViewById(R.id.proteins)).setText(DataManipulator.getStringFromDouble(food.getProteins()));
            ((EditText) findViewById(R.id.salt)).setText(DataManipulator.getStringFromDouble(food.getSalt()));
            Pair<Integer, Integer> time = DataManipulator.getTimeFromDate(food.getDate());
            timePicker.setHour(time.first);
            timePicker.setMinute(time.second);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker = findViewById(R.id.timePicker);
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                double weight = DataManipulator.getDoubleFromEditText(findViewById(R.id.weight));
                double calories = DataManipulator.getDoubleFromEditText(findViewById(R.id.calories));
                double fats = DataManipulator.getDoubleFromEditText(findViewById(R.id.fats));
                double saccharides = DataManipulator.getDoubleFromEditText(findViewById(R.id.saccharides));
                double sugars = DataManipulator.getDoubleFromEditText(findViewById(R.id.sugars));
                double proteins = DataManipulator.getDoubleFromEditText(findViewById(R.id.proteins));
                double salt = DataManipulator.getDoubleFromEditText(findViewById(R.id.salt));
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
}