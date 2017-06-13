package com.webduino.fragment.cardinfo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.webduino.R;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class CardInfo {

    public static final int TYPE_SENSOR = 0;
    public static final int TYPE_TEMPERATURESENSOR = 1;
    public static final int TYPE_DOORSENSOR = 2;
    public static final int TYPE_ONEWIRESENSOR = 3;
    public static final int TYPE_HEATER = 4;
    public static final int TYPE_ACTIONBUTTON = 5;

    public String label;
    public String name;
    public String title;
    public int id;
    public Drawable imageDrawable;

    public boolean online = false;
    private boolean enabled;

    public int labelColor;
    public int labelBackgroundColor;
    public int titleColor;
    public int imageColor;

    public int getSensorType() {
        return TYPE_SENSOR;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setColor(int color) {
        labelBackgroundColor = color;
        labelColor = Color.WHITE;
        titleColor = color;
        imageColor = color;
    }

    public void setImageColor(int color) {
        imageColor = color;
    }
    public void setTitleColor(int color) {
        titleColor = color;
    }
    public void setLabelColor(int color) {
        labelColor = Color.WHITE;
    }
    public void setLabelBackgroundColor(int color) {
        labelBackgroundColor = color;
    }

    public static int getTemperatureColor(double temperature) { // i= 0 - 50

        double max = 50;

        double tmin = 10;
        double tmax = 30;
        if (temperature < tmin) temperature = tmin;
        if (temperature > tmax) temperature = tmax;

        double i = (temperature - tmin) * max / (tmax - tmin);

        int r;
        int b;
        int g;
        double delta = 255.0 / max;

        if (i < (max / 2) ) {
            g = (int) (2 * delta * i);
            b = (int)(255 - 2 * delta * i);
            r = 0;

        } else {
            g = (int) (255 - (2 * delta) * (i - max / 2));
            b = 0;
            r = (int)((2 * delta) * (i - max / 2));

        }
        return Color.rgb(r,g,b);
    }

}