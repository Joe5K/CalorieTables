package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.Common;
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

        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(newImage);

        final EditText name = findViewById(R.id.name);
        final EditText weight = findViewById(R.id.weight);
        final EditText calories = findViewById(R.id.calories);
        final EditText fats = findViewById(R.id.fats);
        final EditText saccharides = findViewById(R.id.saccharides);
        final EditText sugars = findViewById(R.id.sugars);
        final EditText proteins = findViewById(R.id.proteins);
        final EditText salt = findViewById(R.id.salt);

        if (id != 0){
            Food food = dbHelper.getFood(id);
            name.setText(food.getName());
            weight.setText(Common.getStringFromDouble(food.getWeight()));
            calories.setText(Common.getStringFromDouble(food.getCalories()));
            fats.setText(Common.getStringFromDouble(food.getFats()));
            saccharides.setText(Common.getStringFromDouble(food.getSaccharides()));
            sugars.setText(Common.getStringFromDouble(food.getSugars()));
            proteins.setText(Common.getStringFromDouble(food.getProteins()));
            salt.setText(Common.getStringFromDouble(food.getSalt()));
            if(food.getImage()!= null){
                imageBitmap = food.getImage();
                imageView.setImageBitmap(imageBitmap);}
            Pair<Integer, Integer> time = Common.getTimeFromDate(food.getDate());
            timePicker.setHour(time.first);
            timePicker.setMinute(time.second);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long time = dateAtMidnight + timePicker.getMinute()*60000 + timePicker.getHour()*3600000;

                Food food = new Food(
                        name.getText().toString(),
                        Common.getDoubleFromEditText(weight),
                        Common.getDoubleFromEditText(calories),
                        Common.getDoubleFromEditText(fats),
                        Common.getDoubleFromEditText(saccharides),
                        Common.getDoubleFromEditText(sugars),
                        Common.getDoubleFromEditText(proteins),
                        Common.getDoubleFromEditText(salt),
                        time,
                        imageBitmap);

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