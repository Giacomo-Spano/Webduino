package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import com.webduino.MainActivity;
import com.webduino.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giaco on 15/02/2018.
 */

public class OptionCardValue {
    protected Object value;
    protected String name;
    List<OptionCardListener> listeners = new ArrayList<>();
    public String valueDescription = "";

    public OptionCardValue(String name, Object value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public String getStringValue() {
        if (value == null) return "";
        return value.toString();
    }

    public boolean getBoolValue() {
        return false;
    }

    public int getIntValue() {
        return 0;
    }

    public double getDoubleValue() {
        return 0;
    }

    public Date getDateValue() {
        return null;
    }

    public void addListener(OptionCardListener listener) {
        this.listeners.add(listener);
    }

    public interface OptionCardListener {
        void onSetValue(Object value);
    }


    public Object showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);
        // Add the buttons
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //Integer value = new Integer(3);
                if (listeners != null)
                    for(OptionCardListener listener:listeners)
                        listener.onSetValue(value);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });

        LayoutInflater inflater = MainActivity.activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_stringoptionvalue, null));

        /*final CharSequence[] items = {
                "Rajesh", "Mahesh", "Vijayakumar"
        };

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
            }
        });*/

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // show it
        dialog.show();
        return null;
    }
}
