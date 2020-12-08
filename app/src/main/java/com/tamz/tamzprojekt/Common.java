package com.tamz.tamzprojekt;

import android.content.SharedPreferences;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Common {

    public static double getDoubleFromEditText(EditText editText){
        return getDoubleFromString(editText.getText().toString());
    }

    public static double getDoubleFromString(String s){
        try{
            double value = Double.parseDouble(s);
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

    public static long getMidnightTime(long time){
        Calendar rightNow = Calendar.getInstance();

        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);

        long sinceMidnight = (time + offset) %
                (24 * 60 * 60 * 1000);
        return time - sinceMidnight;
    }
}
