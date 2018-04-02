package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by giaco on 15/02/2018.
 */

public class MultiChoiceOptionCardValue extends OptionCardValue {

    private CharSequence[] items;
    private boolean[] itemValues;
    ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items

    public boolean getValue(int i) {
        if (itemValues == null || i < 0 || i > itemValues.length-1)
            return false;
        return itemValues[i];
    }

    public MultiChoiceOptionCardValue(String name, CharSequence[] items, boolean[] itemValues) {
        super(name,itemValues);
        if (items == null)
            return;
        this.items = items;
        this.itemValues = new boolean[items.length];
        int i = 0;
        for(Boolean val:itemValues) {
            if (i < this.itemValues.length)
                this.itemValues[i] = val;
            i++;
        }
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";

        String str = "";
        int i = 0;
        for (Boolean val:itemValues) {
            if (!val) {
                i++;
                continue;
            }
            if (!str.equals(""))
                str += ",";
            str += items[i];
            i++;
        }
        return str;
    }

    public Object showPicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        // Set the dialog title
        builder//.setTitle("R.string.pick_toppings")
                .setMultiChoiceItems(items, itemValues,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                itemValues[which] = isChecked;
                            }
                        })
                // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listeners != null)
                            for (OptionCardListener listener:listeners)
                                listener.onSetValue(value);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
        return null;
    }
}
