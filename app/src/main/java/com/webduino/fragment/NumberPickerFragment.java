package com.webduino.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.webduino.R;

/**
 * Created by Giacomo on 28/12/2014.
 */
public class NumberPickerFragment extends DialogFragment {

    public double value;
    private int decimals;
    private double maxValue, minValue;
    private String tag;
    private String title;
    Handler mHandler;

    public NumberPickerFragment() {
        super();
    }

    public void setNumberHandler(Handler h){
        mHandler = h;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Bundle b = getArguments();
        value = b.getDouble("value");
        decimals = b.getInt("decimals");
        maxValue = b.getDouble("max");
        minValue = b.getDouble("min");
        title = b.getString("title");
        tag = b.getString("tag");

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.number_picker_dialog_layout, null);

        int val = ((int)(value * 10) ) % 1000;

        final NumberPicker np1 = (NumberPicker) v.findViewById(R.id.numberPicker1);
        np1.setMinValue(0);
        np1.setMaxValue(9);
        np1.setValue(val/100);

        final NumberPicker np2 = (NumberPicker) v.findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(9);
        np2.setValue((val % 100) / 10);

        final NumberPicker np3 = (NumberPicker) v.findViewById(R.id.numberPicker3);
        np3.setMinValue(0);
        np3.setMaxValue(9);
        np3.setValue(val % 10);


        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(v)
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Bundle b = new Bundle();

                                value = np1.getValue() * 10 + np2.getValue() + np3.getValue() * 0.1;

                                b.putDouble("value", value);
                                b.putString("tag", tag);
                                Message m = new Message();
                                m.setData(b);
                                mHandler.sendMessage(m);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();

    }
}
