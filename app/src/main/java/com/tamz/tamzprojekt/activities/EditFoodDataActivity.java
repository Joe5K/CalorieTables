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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.Common;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.util.ArrayList;
import java.util.Objects;

public class EditFoodDataActivity extends AppCompatActivity {
    private Bitmap imageBitmap;
    private TextView deleteTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_edit_food_data);

        Intent intent = getIntent();
        final long dateAtMidnight = intent.getLongExtra("DATE", 0);
        final int id = intent.getIntExtra("ID", 0);
        final String action = intent.getStringExtra("ACTION");

        final DBHelper dbHelper = new DBHelper(this);

        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setVisibility(TextView.INVISIBLE);

        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        final Drawable newImage = ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_a_photo_24);
        imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(newImage);


        final EditText name = findViewById(R.id.name);
        final EditText weight = findViewById(R.id.weight);
        final EditText calories = findViewById(R.id.calories);
        final EditText fats = findViewById(R.id.fats);
        final EditText saccharides = findViewById(R.id.saccharides);
        final EditText sugars = findViewById(R.id.sugars);
        final EditText proteins = findViewById(R.id.proteins);
        final EditText salt = findViewById(R.id.salt);


        if (id != 0) {
            Food food = dbHelper.getFood(id);
            name.setText(food.getName());
            weight.setText(Common.getStringFromDouble(food.getWeight()));
            calories.setText(Common.getStringFromDouble(food.getCalories()));
            fats.setText(Common.getStringFromDouble(food.getFats()));
            saccharides.setText(Common.getStringFromDouble(food.getSaccharides()));
            sugars.setText(Common.getStringFromDouble(food.getSugars()));
            proteins.setText(Common.getStringFromDouble(food.getProteins()));
            salt.setText(Common.getStringFromDouble(food.getSalt()));
            if (food.getImage() != null) {
                imageBitmap = food.getImage();
                imageView.setImageBitmap(imageBitmap);
                deleteTextView.setVisibility(TextView.VISIBLE);
            }
            if (!"NEW".equals(action)){
                Pair<Integer, Integer> time = Common.getTimeFromDate(food.getDate());
                timePicker.setHour(time.first);
                timePicker.setMinute(time.second);
            }
        }
        else {
            String searchBarName = intent.getStringExtra("NAME");
            name.setText(searchBarName);

        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long time = dateAtMidnight + timePicker.getMinute() * 60000 + timePicker.getHour() * 3600000;

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

                if ("NEW".equals(action)) {
                    dbHelper.addFood(food);
                }
                else if ("EDIT".equals(action)){
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
                startActivityForResult(takePictureIntent, 1);
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageBitmap = null;
                imageView.setImageDrawable(newImage);
                deleteTextView.setVisibility(TextView.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            deleteTextView.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
