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
    public static final int TYPE_ACTIONBUTTON = 5;
    public static final int TYPE_HEATER = 6;
    public static final int TYPE_SCENARIO = 7;
    public static final int TYPE_OPTION = 8;
    public static final int TYPE_TIMEINTERVAL = 9;
    public static final int TYPE_TRIGGER = 10;
    public static final int TYPE_PROGRAM = 11;
    public static final int TYPE_PROGRAMTIMERANGE = 12;
    public static final int TYPE_PROGRAMACTION = 13;
    public static final int TYPE_WEBDUINOSYSTEM = 14;
    public static final int TYPE_WEBDUINOSYSTEMZONE = 15;
    public static final int TYPE_WEBDUINOSYSTEMACTUATOR = 16;

    public String label;
    public String name;
    public String title;
    public int id;
    public int shieldid;
    public Drawable imageDrawable;

    public boolean online = false;
    public boolean enabled;

    public boolean status = false;

    public int labelColor;
    public int labelBackgroundColor;
    public int titleColor;
    public int imageColor;

    protected int type;

    public CardInfo() {
        type = TYPE_SENSOR;
    }

    public int getType() {
        return type;
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