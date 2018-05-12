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
        if (getValue() == null) return "";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(getValue());
    }

    public String getValueDescription() {
        return getStringValue();
    }

    @Override
    public Date getDateValue() {
        if (getValue() != null)
            return (Date)getValue();
        return null;
    }

    public void showStartTimePickerDialog(Date time) {

        new TimePickerDialog(MainActivity.activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date time = new Date();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                setValue(time);
                if (listeners != null)
                    for (OptionCardListener listener:listeners)
                        listener.onSetValue(getValue());
            }

        }, time.getHours(), time.getMinutes(), false).show();
    }


    public Object showPicker() {

        Date date = new Date();
        if (getValue() != null)
            date = (Date) getValue();
        showStartTimePickerDialog(date);
      return null;
    }

}
