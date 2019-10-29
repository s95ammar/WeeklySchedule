package com.s95ammar.weeklyschedule.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Category;

import java.util.ArrayList;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    public CategorySpinnerAdapter(Context context, ArrayList<Category> categoriesList) {
        super(context, 0, categoriesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row_category, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_category_spinner);

        Category currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
            textViewName.setTextColor(currentItem.getTextColor());
            DrawableCompat.setTint(textViewName.getBackground(), currentItem.getFillColor());

        }

        return convertView;
    }
}
