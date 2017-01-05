package com.webduino.controls;

import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Time;

/**
 * Created by Giacomo Spanò on 31/12/2016.
 */

public class TimePoint {
    protected int value;
    protected double temperature;
    protected String timeStr;
    private GregorianCalendar cal;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TimePoint(double temperature, GregorianCalendar cal) {
        this.temperature = temperature;
        this.cal = cal;

        value = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        timeStr = "" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
}