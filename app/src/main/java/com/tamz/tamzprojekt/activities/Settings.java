package com.tamz.tamzprojekt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamz.tamzprojekt.Common;
import com.tamz.tamzprojekt.R;

public class Settings extends AppCompatActivity {
    private static final String KEY_CALORIES = "Calories";
    private static final String KEY_FATS = "Fats";
    private static final String KEY_SACCHARIDES = "Saccharides";
    private static final String KEY_SUGARS = "Sugars";
    private static final String KEY_PROTEINS = "Proteins";
    private static final String KEY_SALT = "Salt";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE);

        final EditText calories = findViewById(R.id.calories);
        final EditText fats = findViewById(R.id.fats);
        final EditText saccharides = findViewById(R.id.saccharides);
        final EditText sugars = findViewById(R.id.sugars);
        final EditText proteins = findViewById(R.id.proteins);
        final EditText salt = findViewById(R.id.salt);

        calories.setText(getData(sharedPref, KEY_CALORIES, 0));
        fats.setText(getData(sharedPref, KEY_FATS, 0));
        saccharides.setText(getData(sharedPref, KEY_SACCHARIDES, 0));
        sugars.setText(getData(sharedPref, KEY_SUGARS, 0));
        proteins.setText(getData(sharedPref, KEY_PROTEINS, 0));
        salt.setText(getData(sharedPref, KEY_SALT, 0));

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor = putDouble(editor, KEY_CALORIES, Common.getDoubleFromEditText(calories));
                editor = putDouble(editor, KEY_FATS, Common.getDoubleFromEditText(fats));
                editor = putDouble(editor, KEY_SACCHARIDES, Common.getDoubleFromEditText(saccharides));
                editor = putDouble(editor, KEY_SUGARS, Common.getDoubleFromEditText(sugars));
                editor = putDouble(editor, KEY_PROTEINS, Common.getDoubleFromEditText(proteins));
                editor = putDouble(editor, KEY_SALT, Common.getDoubleFromEditText(salt));
                editor.apply();
                finish();
            }
        });
    }

    public static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static String getData(final SharedPreferences prefs, final String key, final double defaultValue) {
        double num =  Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
        return Common.getStringFromDouble(num);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}