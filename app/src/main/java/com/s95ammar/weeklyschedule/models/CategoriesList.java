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

}
