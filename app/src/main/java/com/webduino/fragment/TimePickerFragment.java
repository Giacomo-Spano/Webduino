package com.webduino.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TimePicker;

import com.webduino.R;


@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment{
    private Handler mHandler ;
    private int mHour;
    private int mMinute;
    private String mMessage;
    private String mTitle;
    private String tag;

    private int minHour = 0;
    private int minMinute = 0;

    private int maxHour = 24;
    private int maxMinute = 59;

	public TimePickerFragment(Handler h){
        mHandler = h;
    }

    TimePickerDialog.OnTimeSetListener listener  = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {

        }
    };

    public void setMin(int hour, int minute) {
        minHour = hour;
        minMinute = minute;
    }

    public void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }

    public void setCurrentTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    public void setMessage(String title, String message) {
        mMessage = message;
        mTitle = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final TimeRangeTimePickerDialog tp = new TimeRangeTimePickerDialog(getActivity(), listener, mHour, mMinute,true);
        tp.setTitle(mTitle);
        tp.setMessage(mMessage);
        tp.setMin(mHour,mMinute);
        tp.setMin(minHour,minMinute);
        tp.setMax(maxHour,maxMinute);

        tp.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    Bundle b = new Bundle();
                    b.putInt("hour", tp.getCurrentHour());
                    b.putInt("minute", tp.getCurrentMinute());
                    b.putString("tag", tag);
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
