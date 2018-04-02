package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;

/**
 * Created by giaco on 15/02/2018.
 */

public class IntegerOptionCardValue extends OptionCardValue {

    public IntegerOptionCardValue(String name, Integer value) {
        super(name,value);
    }

    @Override
    public int getIntValue() {
        if (value == null) return 0;
        return (int) value;
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";
        return "" + value;
    }


    public Object showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        LayoutInflater inflater = MainActivity.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_numberoptionvalue, null);
        final TextView tv = view.findViewById(R.id.numberTextView);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CharSequence str = tv.getText();
                        value = Integer.valueOf(str.toString());

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
