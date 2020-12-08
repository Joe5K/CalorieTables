package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tamz.tamzprojekt.FoodsArrayAdapter;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;
import com.tamz.tamzprojekt.database.Food;

import java.util.ArrayList;
import java.util.Objects;

public class NewFoodActivity extends AppCompatActivity {
    DBHelper dbHelper;
    long dateAtMidnight;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        Intent intent = getIntent();
        dateAtMidnight = intent.getLongExtra("DATE", 0);
        dbHelper = new DBHelper(this);

        final SearchView searchView = findViewById(R.id.searchView);

        Button newButton = findViewById(R.id.buttonNew);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEditScreen(searchView.getQuery().toString());
            }
        });

        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = (Food) listView.getItemAtPosition(i);

                loadEditScreen(food);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return onQueryTextSubmit(query);
            }
        });

        filter("");
    }

    private void filter(String query){
        FoodsArrayAdapter adapter = new FoodsArrayAdapter(this, dbHelper.getAllFoodsWith(query), true);
        listView.setAdapter(adapter);
    }

    public void loadEditScreen(String name){
        Intent intent = new Intent(this, EditFoodDataActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("DATE", dateAtMidnight);
        intent.putExtra("ACTION", "NEW");
        startActivityForResult(intent,1);
    }

    public void loadEditScreen(Food food){
        Intent intent = new Intent(this, EditFoodDataActivity.class);
        intent.putExtra("ID", food.getId());
        intent.putExtra("DATE", dateAtMidnight);
        intent.putExtra("ACTION", "NEW");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}