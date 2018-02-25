package com.webduino.fragment.cardinfo.optioncardvalue;

import com.webduino.MainActivity;
import com.webduino.fragment.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by giaco on 15/02/2018.
 */

public class DateOptionCardValue extends OptionCardValue {

    public DateOptionCardValue(String name, Date value) {
        super(name,value);
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df.format(value);
    }

    @Override
    public Date getDateValue() {
        if (value != null)
            return (Date)value;
        return null;
    }

    public void showStartDateTimePickerDialog(Date datetime) {
        DatePickerFragment dateFragment= new DatePickerFragment();
        dateFragment.setDate(datetime);
        dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
            @Override
            public void setDate(Date date) {
                value = date;
                if (listener != null)
                    listener.onSetValue(value);
            }
            @Override
            public void cancel() {

            }
        });
        dateFragment.show(MainActivity.activity.getFragmentManager(), "Data inizio");
    }

    public Object showPicker() {

        Date date = new Date();
        if (value != null)
            date = (Date) value;
        showStartDateTimePickerDialog(date);
      return null;
    }
}
