package com.tamz.tamzprojekt.database;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Food {
    private int id;
    private String name;
    private double weight, calories, fats, saccharides, sugars, proteins, salt;
    private long date;
    private Bitmap image;

    public Food(String name, double weight, double calories, double fats, double sacharids, double sugars, double proteins, double salt, long date){//, Bitmap image) { TODO
        this.name = name;
        this.weight = weight;
        this.calories = calories;
        this.fats = fats;
        this.saccharides = sacharids;
        this.sugars = sugars;
        this.proteins = proteins;
        this.salt = salt;
        this.date = date;
        //this.image = image;
    }

    public Food(int id, String name, double weight, double calories, double fats, double sacharids, double sugars, double proteins, double salt, long date){//, Bitmap image) { TODO
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.calories = calories;
        this.fats = fats;
        this.saccharides = sacharids;
        this.sugars = sugars;
        this.proteins = proteins;
        this.salt = salt;
        this.date = date;
        //this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setSaccharides(double saccharides) {
        this.saccharides = saccharides;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getCalories() {
        return calories;
    }

    public double getFats() {
        return fats;
    }

    public double getSaccharides() {
        return saccharides;
    }

    public double getSugars() {
        return sugars;
    }

    public double getProteins() {
        return proteins;
    }

    public double getSalt() {
        return salt;
    }

    public String getDateStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(new Date(date));
        return dateString;
    }

    public long getDate(){ return this.date; }

    public Bitmap getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(new Date(date));
        return dateString + " - " + name;
    }
}
