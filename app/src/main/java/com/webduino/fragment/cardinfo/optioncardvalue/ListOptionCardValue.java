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
        if (value >= 0 && value < items.length)
            valueDescription = (String) items[value];

    }

    @Override
    protected void setValue(Object value) {
        super.setValue(value);
    }

    public ListOptionCardValue(String name, Integer value, CharSequence[] items, String[] itemStringValues) {
        super(name, value);
        if (items == null)
            return;
        this.items = items;
        this.itemStringValues = itemStringValues;

        // annulla itemintvsluer perchè è una lista di stringhe
        itemIntValues = null;

        valueDescription = "---";
        if (value >= 0 && value < items.length)
            valueDescription = (String) items[value];
    }

    @Override
    public int getIntValue() {

        if (itemIntValues == null || getValue() == null || !(getValue() instanceof Integer)) return 0;
        //return (Integer)getValue();
        return itemIntValues[((Integer) getValue()).intValue()];
    }

    @Override
    public String getStringValue() {

        if (!(getValue() instanceof Integer))
            return "errore";

        if (items == null || getValue() == null)
            return "";

        int n = (Integer) getValue();
        if (n >= items.length)
            return "errore";

        if (itemIntValues != null) {
            return "" + items[n];
        } else if (itemStringValues != null) {
            return itemStringValues[n];
        }
        return "errore";
    }

    @Override
    public String getValueDescription() {
        return valueDescription;
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
                                    setValue(itemIntValues[which]);
                                    listener.onSetValue(getValue());
                                }
                            } else {
                                for (OptionCardListener listener : listeners) {
                                    setValue(which);
                                    listener.onSetValue(getStringValue());
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
