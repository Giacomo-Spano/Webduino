package com.webduino.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TimePicker;

import com.webduino.R;


@SuppressLint("ValidFragment")
public class TimePickerDialogFragment extends DialogFragment{
    Handler mHandler ;
    int mHour;
    int mMinute;
    String mMessage;
    String mTitle;

    
    //@SuppressLint("ValidFragment")
	public TimePickerDialogFragment(Handler h){
        mHandler = h;
    }

    TimePickerDialog.OnTimeSetListener listener  = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {

        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
 
        Bundle b = getArguments();
        mHour = b.getInt("hour");
        mMinute = b.getInt("minute");
        mTitle = b.getString("title");
        mMessage = b.getString("message");

        final TimeRangeTimePickerDialog tp = new TimeRangeTimePickerDialog(getActivity(), listener, mHour, mMinute,true);
        tp.setTitle("ora");
        tp.setMessage(mMessage);
        tp.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    Bundle b = new Bundle();
                    b.putInt("hour", tp.m_hourOfDay);
                    b.putInt("minute", tp.m_minute);
                    Message m = new Message();
                    m.setData(b);
                    mHandler.sendMessage(m);
                }
            }
        });
        tp.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    //timeSet = false;
                }
            }
        });
        tp.setTitle(mMessage);
        tp.setIcon(R.drawable.ic_menu_gallery);

        return tp;
    }
}
