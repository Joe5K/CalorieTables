package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.tamz.tamzprojekt.Common;
import com.tamz.tamzprojekt.R;
import com.tamz.tamzprojekt.database.DBHelper;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.util.HashMap;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private static final String KEY_CALORIES = "Calories";
    private static final String KEY_FATS = "Fats";
    private static final String KEY_SACCHARIDES = "Saccharides";
    private static final String KEY_SUGARS = "Sugars";
    private static final String KEY_PROTEINS = "Proteins";
    int RED = Color.parseColor("#FF0000");
    int BLACK = Color.parseColor("#000000");

    private HashMap<String, Double> dailyStats;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent intent = getIntent();
        long date = Common.getMidnightTime(intent.getLongExtra("DATE", 0));

        DBHelper dbHelper = new DBHelper(this);
        dailyStats = dbHelper.getDailyStats(date);
        sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE);

        StackedBarChart stackedBarChart = findViewById(R.id.stackedbarchart);

        stackedBarChart.addBar(generateStackedBarModel(KEY_CALORIES));
        stackedBarChart.addBar(generateStackedBarModel(KEY_FATS));
        stackedBarChart.addBar(generateStackedBarModel(KEY_SACCHARIDES));
        stackedBarChart.addBar(generateStackedBarModel(KEY_SUGARS));
        stackedBarChart.addBar(generateStackedBarModel(KEY_PROTEINS));

        stackedBarChart.startAnimation();
    }

    private StackedBarModel generateStackedBarModel(String key) {
        double goal = getData(sharedPref, key);
        double summed = dailyStats.get(key);


        StackedBarModel stackedBarModel = new StackedBarModel(key);

        if (goal>summed){
            stackedBarModel.addBar(new BarModel((float) summed, 0xFF0223b6));
            stackedBarModel.addBar(new BarModel((float) (goal-summed), 0xFF02B605));
        }
        else{
            stackedBarModel.addBar(new BarModel((float) goal, 0xFFFF0000));
        }

        return stackedBarModel;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private static double getData(final SharedPreferences prefs, final String key) {
        double num =  Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(0.0)));
        return num;
    }
}