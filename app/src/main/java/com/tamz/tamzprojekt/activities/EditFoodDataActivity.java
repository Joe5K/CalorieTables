package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private Drawable newImage;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
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
        newImage = ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_a_photo_24);
        setContentView(R.layout.activity_edit_food_data);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(newImage);

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
            if(food.getImage()!= null){
                imageBitmap = food.getImage();
                imageView.setImageBitmap(imageBitmap);}
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

                Food food = new Food(name, weight, calories, fats, saccharides, sugars, proteins, salt, time, imageBitmap);

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        /*imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageView.setImageDrawable(newImage);
                return true;
            }
        });*/
    }
}