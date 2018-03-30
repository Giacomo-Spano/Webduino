package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.widget.TimePicker;

import com.webduino.MainActivity;
import com.webduino.fragment.DatePickerFragment;
import com.webduino.fragment.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by giaco on 15/02/2018.
 */

public class TimeOptionCardValue extends OptionCardValue {

    public TimeOptionCardValue(String name, Date value) {
        super(name,value);
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(value);
    }

    @Override
    public Date getDateValue() {
        if (value != null)
            return (Date)value;
        return null;
    }

    public void showStartTimePickerDialog(Date time) {

        new TimePickerDialog(MainActivity.activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date time = new Date();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                value = time;
                if (listener != null)
                    listener.onSetValue(value);
            }

        }, time.getHours(), time.getMinutes(), false).show();
    }


    public Object showPicker() {

        Date date = new Date();
        if (value != null)
            date = (Date) value;
        showStartTimePickerDialog(date);
      return null;
    }

}
