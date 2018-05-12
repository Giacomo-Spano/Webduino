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
        if (value)
            valueDescription = trueDescription;
        else
            valueDescription = falseDescription;
    }

    @Override
    public String getValueDescription() {
        if (getValue() == null) return "";
        boolean checked = (boolean) getValue();
        if (checked)
            return trueDescription;
        else
            return falseDescription;
    }

    @Override
    public boolean getBoolValue() {
        if (getValue() == null)
            return false;
        return (boolean)getValue();
    }

    @Override
    public String getStringValue() {
        if (getValue() == null) return "";
        boolean checked = (boolean) getValue();
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
        cb.setChecked((boolean) getValue());

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cb.isChecked()) {
                            setValue(new Boolean(true));
                            valueDescription = trueDescription;
                        } else {
                            setValue(new Boolean(false));
                            valueDescription = falseDescription;
                        }

                        if (listeners != null)
                            for (OptionCardListener listener:listeners)
                                listener.onSetValue(getValue());
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
