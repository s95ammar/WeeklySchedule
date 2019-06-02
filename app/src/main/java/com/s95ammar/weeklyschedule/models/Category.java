package com.s95ammar.weeklyschedule.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private int fillColor;
    private int textColor;

    public Category(String name, int fillColor, int textColor) {
        this.name = name;
        this.fillColor = fillColor;
        this.textColor = textColor;
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
}
