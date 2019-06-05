package com.s95ammar.weeklyschedule.models;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class CategoriesList extends ArrayList<Category> {
    private static final String TAG = "CategoriesList";
    private static CategoriesList instance;

    private CategoriesList() {
    }

    public static synchronized CategoriesList getInstance() {
        if (instance == null) {
            instance = new CategoriesList();
        }
        return instance;
    }


    public static void createFromJson(String json) {
        Log.d(TAG, "createFromJson: " + json);
        if (instance == null) {
            Gson gson = LocalTimeSerializer.getGsonLocalTimeSerializer();
            instance = gson.fromJson(json, CategoriesList.class);
            Log.d(TAG, "createFromJson: " + getInstance().toString());
        } else {
            Log.d(TAG, "createFromJson: Categories list already exists");
        }


    }


}
