package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import com.webduino.MainActivity;
import com.webduino.R;

import java.util.Date;

/**
 * Created by giaco on 15/02/2018.
 */

public class OptionCardValue {
    protected Object value;
    protected String name;
    OptionCardListener listener = null;

    public OptionCardValue(String name, Object value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /*public Object getValue() {
        return value;
    }*/

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

    public void setListener(OptionCardListener listener) {
        this.listener = listener;
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
                if (listener != null)
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
