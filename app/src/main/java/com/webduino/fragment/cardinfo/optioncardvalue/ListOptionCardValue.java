package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.webduino.MainActivity;

import java.util.ArrayList;

/**
 * Created by giaco on 15/02/2018.
 */

public class ListOptionCardValue extends OptionCardValue {

    private CharSequence[] items;
    private int[] itemValues;

    @Override
    public int getIntValue() {
        if (value == null) return 0;
        return (int)value;
    }

    public ListOptionCardValue(String name, Integer value, CharSequence[] items, int[] itemValues) {
        super(name, value);
        if (items == null)
            return;
        this.items = items;
        this.itemValues = itemValues;
    }

    @Override
    public String getStringValue() {

        if (items == null || value == null )
            return "";
        int n = (Integer) value;
        if (n >= items.length)
            return "";
        return items[n].toString();
    }

    public Object showPicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        // Set the dialog title
        builder//.setTitle("R.string.pick_toppings")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        value = which;
                        if (listener != null)
                            listener.onSetValue(itemValues[which]);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return null;
    }
}
