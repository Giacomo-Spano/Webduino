package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.webduino.MainActivity;

/**
 * Created by giaco on 15/02/2018.
 */

public class ListOptionCardValue extends OptionCardValue {

    private CharSequence[] items;
    private int[] itemIntValues;
    private String[] itemStringValues;



    public ListOptionCardValue(String name, Integer value, CharSequence[] items, int[] itemIntValues) {
        super(name, value);
        if (items == null)
            return;
        this.items = items;
        this.itemIntValues = itemIntValues;
        itemStringValues = null;

        valueDescription = "---";
        for (int i = 0; i < itemIntValues.length; i++) {
            if (itemIntValues[i]==value)
                valueDescription = (String) items[i];
        }
    }

    public ListOptionCardValue(String name, String value, CharSequence[] items, String[] itemStringValues) {
        super(name, value);
        if (items == null)
            return;
        this.items = items;
        this.itemStringValues = itemStringValues;
        itemIntValues = null;
    }

    @Override
    public int getIntValue() {

        if (itemIntValues == null)
            return -1;

        if (value == null) return 0;
        return (int)value;
    }

    @Override
    public String getStringValue() {


            if (items == null || value == null)
                return "";
        if (itemIntValues != null) {
            int n = (Integer) value;
            if (n >= items.length)
                return "";
            return "" + itemIntValues[n];
        } else {
            return (String) value;
        }
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
                        valueDescription = (String) items[which];
                        if (listeners != null) {
                            if (itemIntValues != null) {
                                for (OptionCardListener listener:listeners)
                                    listener.onSetValue(itemIntValues[which]);
                            } else {
                                for (OptionCardListener listener:listeners)
                                    listener.onSetValue(itemStringValues[which]);
                            }
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return null;
    }
}
