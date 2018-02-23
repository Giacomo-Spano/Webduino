package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;

/**
 * Created by giaco on 15/02/2018.
 */

public class BooleanOptionCardValue extends OptionCardValue {

    String trueDescription, falseDescription;

    public BooleanOptionCardValue(String name, Boolean value, String trueDescription, String falseDescription) {
        super(name,value);
        this.trueDescription = trueDescription;
        this.falseDescription = falseDescription;
    }

    public boolean getValue() {
        if (value == null)
            return false;
        return (boolean)value;
    }

    @Override
    public boolean getBoolValue() {
        if (value == null)
            return false;
        return (boolean)value;
    }

    @Override
    public String getStringValue() {
        if (value == null) return "";
        boolean checked = (boolean) value;
        if (checked)
            return trueDescription;
        else
            return falseDescription;
    }

    public Object showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        LayoutInflater inflater = MainActivity.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_booleanoptionvalue, null);
        final CheckBox cb = view.findViewById(R.id.checkBox);

        cb.setText(trueDescription);
        cb.setChecked((boolean) value);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cb.isChecked())
                            value = new Boolean(true);
                        else
                            value = new Boolean(false);

                        if (listener != null)
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
