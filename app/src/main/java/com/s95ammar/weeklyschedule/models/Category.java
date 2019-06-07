package com.s95ammar.weeklyschedule.models;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class Category implements Serializable {
    @Expose
    private String name;
    @Expose
    private int fillColor;
    @Expose
    private int textColor;
    private TreeSet<Event> categoryEvents;

    public Category(String name, int fillColor, int textColor, TreeSet<Event> categoryEvents) {
        this.name = name;
        this.fillColor = fillColor;
        this.textColor = textColor;
        this.categoryEvents = categoryEvents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public TreeSet<Event> getCategoryEvents() {
        return categoryEvents;
    }

    public void setCategoryEvents(TreeSet<Event> categoryEvents) {
        this.categoryEvents = categoryEvents;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            return name.equals(((Category) obj).getName()) && fillColor == ((Category) obj).fillColor && textColor == ((Category) obj).textColor;
        }
        return false;
    }

    public ArrayList<String> getCategoryEventsNames() {
        ArrayList<Event> events = new ArrayList<>(categoryEvents);
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            names.add(events.get(i).getName());
        }
        return names;
    }

    @Override
    public String toString() {
        return '{' + name + ':' + fillColor + ", " + textColor +  '}';
    }

}
