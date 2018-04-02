package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;

/**
 * Created by giaco on 15/02/2018.
 */

public class StringOptionCardValue extends OptionCardValue {

    public StringOptionCardValue(String name, String value) {
        super(name,value);
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";
        return value.toString();
    }

    public Object showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        LayoutInflater inflater = MainActivity.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_stringoptionvalue, null);

        TextView tv = view.findViewById(R.id.titleTextView);
        tv.setText(name);

        final EditText et = view.findViewById(R.id.stringEditText);
        et.setText(getStringValue());

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CharSequence str = et.getText();
                        value = str.toString();
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
