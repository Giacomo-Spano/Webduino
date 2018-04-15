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
            if (itemIntValues[i] == value)
                valueDescription = (String) items[i];
        }
    }

    public ListOptionCardValue(String name, Integer value, CharSequence[] items, String[] itemStringValues) {
        super(name, value);

        if (itemIntValues != null) {
            this.value = "" + items[value];
        } else if (itemStringValues != null) {
            this.value = itemStringValues[value];
        }

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
        return (int) itemIntValues[(int)value];
    }

    @Override
    public String getStringValue() {

        if (!(value instanceof Integer))
            return "errore";

        if (items == null || value == null)
            return "";

        int n = (Integer) value;
        if (n >= items.length)
            return "errore";

        if (itemIntValues != null) {
            return "" + items[n];
        } else if (itemStringValues != null) {
            return itemStringValues[n];
        } else {
            return "errore";
        }
    }

    @Override
    public String getValueDescription() {
        return getStringValue();
    }


    public Object showPicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        // Set the dialog title
        builder//.setTitle("R.string.pick_toppings")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        //value = which;
                        valueDescription = (String) items[which];
                        if (listeners != null) {
                            if (itemIntValues != null) {
                                for (OptionCardListener listener : listeners) {
                                    value = itemIntValues[which];
                                    listener.onSetValue(value);
                                }
                            } else {
                                for (OptionCardListener listener : listeners) {
                                    value = items[which];
                                    listener.onSetValue(value);
                                }
                            }
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return null;
    }
}
