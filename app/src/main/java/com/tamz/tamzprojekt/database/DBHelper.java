package com.tamz.tamzprojekt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "CalorieTables.db";
    private static final String TABLE_FOODS = "Foods";
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_WEIGHT = "Weight";
    private static final String KEY_CALORIES = "Calories";
    private static final String KEY_FATS = "Fats";
    private static final String KEY_SACCHARIDES = "Saccharides";
    private static final String KEY_SUGARS = "Sugars";
    private static final String KEY_PROTEINS = "Proteins";
    private static final String KEY_SALT = "Salt";
    private static final String KEY_DATE = "Date";
    private static final String KEY_IMAGE = "Image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FOODS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_WEIGHT + " REAL, " +
                KEY_CALORIES + " REAL, " +
                KEY_FATS + " REAL, " +
                KEY_SACCHARIDES + " REAL, " +
                KEY_SUGARS + " REAL, " +
                KEY_PROTEINS + " REAL, " +
                KEY_SALT + " REAL, " +
                KEY_DATE + " INTEGER, " +
                KEY_IMAGE + " BLOB);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        onCreate(db);
    }

    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, food.getName());
        values.put(KEY_WEIGHT, food.getWeight());
        values.put(KEY_CALORIES, food.getCalories());
        values.put(KEY_FATS, food.getFats());
        values.put(KEY_SACCHARIDES, food.getSaccharides());
        values.put(KEY_SUGARS, food.getSugars());
        values.put(KEY_PROTEINS, food.getProteins());
        values.put(KEY_SALT, food.getSalt());
        values.put(KEY_DATE, food.getDate());
        values.put(KEY_IMAGE, getByteArrayFromBitmap(food.getImage()));

        db.insert(TABLE_FOODS, null, values);

        db.close();
    }

    // code to get the single contact
    public Food getFood(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS, new String[]{KEY_ID,
                        KEY_NAME, KEY_WEIGHT, KEY_CALORIES, KEY_FATS, KEY_SACCHARIDES, KEY_SUGARS, KEY_PROTEINS, KEY_SALT, KEY_DATE, KEY_IMAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return contact
        Food food = new Food(
                cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT)),
                cursor.getDouble(cursor.getColumnIndex(KEY_CALORIES)),
                cursor.getDouble(cursor.getColumnIndex(KEY_FATS)),
                cursor.getDouble(cursor.getColumnIndex(KEY_SACCHARIDES)),
                cursor.getDouble(cursor.getColumnIndex(KEY_SUGARS)),
                cursor.getDouble(cursor.getColumnIndex(KEY_PROTEINS)),
                cursor.getDouble(cursor.getColumnIndex(KEY_SALT)),
                cursor.getLong(cursor.getColumnIndex(KEY_DATE)),
                getBitmapFromByteArray(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))));
        cursor.close();
        return food;
    }


    public HashMap<String, Double> getDailyStats(long date) {
        ArrayList<Food> foods = getFoodsInDay(date);

        double calories = 0;
        double fats = 0;
        double saccharides = 0;
        double sugars = 0;
        double proteins = 0;

        for (Food food: foods) {
            calories+=(food.getWeight()*(food.getCalories()/100));
            fats+=(food.getWeight()*(food.getFats()/100));
            saccharides+=(food.getWeight()*(food.getSaccharides()/100));
            sugars+=(food.getWeight()*(food.getSugars()/100));
            proteins+=(food.getWeight()*(food.getProteins()/100));
        }

        HashMap<String, Double> dailyStats = new HashMap<String, Double>();
        dailyStats.put(KEY_CALORIES, calories);
        dailyStats.put(KEY_FATS, fats);
        dailyStats.put(KEY_SACCHARIDES, saccharides);
        dailyStats.put(KEY_SUGARS, sugars);
        dailyStats.put(KEY_PROTEINS, proteins);

        return dailyStats;
    }


    public ArrayList<Food> getFoodsInDay(long date) {
        ArrayList<Food> foodList = new ArrayList<Food>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOODS, new String[]{KEY_ID,
                        KEY_NAME, KEY_WEIGHT, KEY_CALORIES, KEY_FATS, KEY_SACCHARIDES, KEY_SUGARS, KEY_PROTEINS, KEY_SALT, KEY_DATE, KEY_IMAGE}, KEY_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(date), String.valueOf(getTomorrowDate(date))}, null, null, KEY_DATE, null);

        if (cursor != null)
            cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            foodList.add(new Food(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_CALORIES)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_FATS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SACCHARIDES)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SUGARS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_PROTEINS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SALT)),
                    cursor.getLong(cursor.getColumnIndex(KEY_DATE)),
                    getBitmapFromByteArray(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)))));
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println(foodList.size());
        return foodList;
    }

    public void deleteFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOODS, KEY_ID + " = ?",
                new String[]{String.valueOf(food.getId())});
        db.close();
    }

    public void editFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_NAME,food.getName());
        contentValues.put(KEY_WEIGHT,food.getWeight());
        contentValues.put(KEY_CALORIES,food.getCalories());
        contentValues.put(KEY_FATS,food.getFats());
        contentValues.put(KEY_SACCHARIDES,food.getSaccharides());
        contentValues.put(KEY_SUGARS,food.getSugars());
        contentValues.put(KEY_PROTEINS,food.getProteins());
        contentValues.put(KEY_SALT,food.getSalt());
        contentValues.put(KEY_DATE,food.getDate());
        contentValues.put(KEY_IMAGE, getByteArrayFromBitmap(food.getImage()));

        db.update(TABLE_FOODS, contentValues,"ID=?",new String[] {Integer.toString(food.getId())});
    }

    public ArrayList<Food> getAllFoodsWith(String query) {
        ArrayList<Food> foodList = new ArrayList<Food>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOODS, new String[]{KEY_ID,
                        KEY_NAME, KEY_WEIGHT, KEY_CALORIES, KEY_FATS, KEY_SACCHARIDES, KEY_SUGARS, KEY_PROTEINS, KEY_SALT, KEY_DATE, KEY_IMAGE}, KEY_NAME + " LIKE ?",
                new String[]{"%"+query+"%"}, "LOWER("+KEY_NAME+")", null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            foodList.add(new Food(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_CALORIES)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_FATS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SACCHARIDES)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SUGARS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_PROTEINS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_SALT)),
                    cursor.getLong(cursor.getColumnIndex(KEY_DATE)),
                    getBitmapFromByteArray(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)))));
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println(foodList.size());
        return foodList;
    }

    private static long getTomorrowDate(long date){
        return date + 86400000;
    }

    private static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private static Bitmap getBitmapFromByteArray(byte[] data){
        if (data == null)
            return null;

        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
