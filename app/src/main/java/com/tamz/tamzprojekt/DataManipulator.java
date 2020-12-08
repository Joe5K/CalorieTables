package com.tamz.tamzprojekt;

import android.content.SharedPreferences;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataManipulator {

    public static double getDoubleFromEditText(View view){
        try{
            double value = Double.parseDouble(((EditText) view).getText().toString());
            return value < 0 ? 0 : value;
        }
        catch (Exception e){
            return 0;
        }
    }

    public static String getStringFromDouble(double d){
        if(d == (long) d)
            return String.format(Locale.getDefault(), "%d",(long) d);
        else
            return String.format("%s",d);
    }

    public static Pair<Integer, Integer> getTimeFromDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String selectedDate = sdf.format(new Date(time));
        return new Pair<>(Integer.parseInt(selectedDate.substring(0,2)), Integer.parseInt(selectedDate.substring(3,5)));
    }
}
