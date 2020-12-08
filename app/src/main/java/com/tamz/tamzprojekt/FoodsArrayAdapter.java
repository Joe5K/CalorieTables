package com.tamz.tamzprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tamz.tamzprojekt.database.Food;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FoodsArrayAdapter extends ArrayAdapter<Food> {
    private final Context context;
    private final ArrayList<Food> foods;
    private boolean isSearching = false;

    public FoodsArrayAdapter(Context context, ArrayList<Food> foods) {
        super(context, -1, foods);
        this.context = context;
        this.foods = foods;
    }

    public FoodsArrayAdapter(Context context, ArrayList<Food> foods, boolean isSearching) {
        super(context, -1, foods);
        this.context = context;
        this.foods = foods;
        this.isSearching = isSearching;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_foods, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(foods.get(position).getImage() != null)
            imageView.setImageBitmap(foods.get(position).getImage());
        else
            imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_fastfood_24));
        name.setText(foods.get(position).getName());
        if (!isSearching)
            date.setText(foods.get(position).getDateStr());
        // change the icon for Windows and iPhone


        return rowView;
    }
}
