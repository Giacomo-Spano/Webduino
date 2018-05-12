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
        if (getValue() == null) return "";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df.format(getValue());
    }

    @Override
    public Date getDateValue() {
        if (getValue() != null)
            return (Date)getValue();
        return null;
    }

    public void showStartDateTimePickerDialog(Date datetime) {
        DatePickerFragment dateFragment= new DatePickerFragment();
        dateFragment.setDate(datetime);
        dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
            @Override
            public void setDate(Date date) {
                setValue(date);
                if (listeners != null)
                    for (OptionCardListener listener:listeners)
                        listener.onSetValue(getValue());
            }
            @Override
            public void cancel() {

            }
        });
        dateFragment.show(MainActivity.activity.getFragmentManager(), "Data inizio");
    }

    public Object showPicker() {

        Date date = new Date();
        if (getValue() != null)
            date = (Date) getValue();
        showStartDateTimePickerDialog(date);
      return null;
    }
}
