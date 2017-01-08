package com.webduino.fragment.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.webduino.R;
import com.webduino.elements.TimeRange;
import com.webduino.fragment.NumberPickerFragment;
import com.webduino.fragment.TimePickerFragment;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.TimeRangeCardInfo;
import com.webduino.wizard.WizardActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class TimeRangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> timeRangeList;

    OnListener mCallback;

    public interface OnListener {
        void onTimeRangeClick(int id);
        void onAddTimeRangeBelow(int id);
        void onDeleteTimeRange(int id);
    }

    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_TIMERANGE = 1;
    //private static final int TYPE_HEATER = 2;

    public TimeRangeAdapter(List<CardInfo> timeRangeList) {
        this.timeRangeList = timeRangeList;
    }

    public void swap(List list) {
        if (timeRangeList != null) {
            timeRangeList.clear();
            timeRangeList.addAll(list);
        } else {
            timeRangeList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return timeRangeList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = timeRangeList.get(i);

        switch (viewHolder.getItemViewType()) {

            /*case TYPE_HEATER:
                break;*/

            case TYPE_TIMERANGE:
                TimeRangeViewHolder timeRangeViewHolder = (TimeRangeViewHolder) viewHolder;
                TimeRangeCardInfo pci = (TimeRangeCardInfo) ci;

                timeRangeViewHolder.timeRange = pci.timeRange;
                timeRangeViewHolder.title.setText(pci.name);

                timeRangeViewHolder.update();

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        /*if (timeRangeList.get(position) instanceof HeaterCardInfo )
            return TYPE_HEATER;
        else */
        if (timeRangeList.get(position) instanceof TimeRangeCardInfo)
            return TYPE_TIMERANGE;

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_PROGRAM;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TIMERANGE) {
            return (RecyclerView.ViewHolder) new TimeRangeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timerange, parent, false));
        } /*else if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        }*/
        return null;
    }

    public class TimeRangeViewHolder extends RecyclerView.ViewHolder {

        protected EditText title;
        protected TimeRange timeRange;
        protected EditText endTime;
        protected EditText startTime;
        protected EditText temperature;
        protected Button addButton;
        protected Button deleteButton;

        protected Handler numberHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    Double value = bundle.getDouble("value");
                    String tag = bundle.getString("tag");
                    if (tag.equals("temperature")) {
                        timeRange.temperature = value;
                        temperature.setText(value.toString());
                    }
                }
            }
        };

        public TimeRangeViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mCallback.onTimeRangeClick(timeRange.ID);
                }
            });

            title = (EditText) v.findViewById(R.id.titleEditText);
            endTime = (EditText) v.findViewById(R.id.endTimeEditText);
            startTime = (EditText) v.findViewById(R.id.startTimeEditText);
            temperature = (EditText) v.findViewById(R.id.temperatureEditText);
            addButton = (Button) v.findViewById(R.id.addButton);
            deleteButton = (Button) v.findViewById(R.id.deleteButton);

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                            Bundle bundle = msg.getData();
                            if (bundle != null) {
                                int hours = bundle.getInt("hour");
                                int minutes = bundle.getInt("minute");
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, hours);
                                cal.set(Calendar.MINUTE, minutes);
                                SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                                endTime.setText(df.format(cal.getTime()));
                            }
                        }
            };

            endTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int minutes = 0;
                    int hours = 0;
                    if (timeRange != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timeRange.endTime);
                        minutes = cal.get(Calendar.MINUTE);
                        hours = cal.get(Calendar.HOUR);
                    }
                    showTimePickerDialog(hours,minutes,"titolo", "messaggio", handler);
                }
            });

            temperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showNumberPickerDialog(timeRange.temperature,1,100,0,"imposta temperatura", "temperature",numberHandler);
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onAddTimeRangeBelow(timeRange.ID);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onDeleteTimeRange(timeRange.ID);
                }
            });



        }

        public void showTimePickerDialog(int mHour, int mMinute, String title, String message, Handler mHandler) {

            Bundle b = new Bundle();
            b.putInt("hour", mHour);
            b.putInt("minute", mMinute);
            b.putString("message", message);
            b.putString("title", title);

            TimePickerFragment timePicker = new TimePickerFragment(
                    mHandler);
            timePicker.setArguments(b);
            FragmentManager fm = WizardActivity.activity.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(timePicker, message/*"time_picker"*/);
            ft.commit();
        }

        public void showNumberPickerDialog(double value, int decimals, double max, double min, String title, String tag, Handler mHandler) {

            Bundle b = new Bundle();
            b.putDouble("value", value);
            b.putInt("decimals", decimals);
            b.putDouble("max", max);
            b.putDouble("min", min);
            b.putString("title", title);
            b.putString("tag", tag);

            NumberPickerFragment numberPickerFragment = new NumberPickerFragment();
            numberPickerFragment.setNumberHandler(numberHandler);
            numberPickerFragment.setArguments(b);
            FragmentManager fm = WizardActivity.activity.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(numberPickerFragment, "numbr_picker");
            ft.commit();
        }

        public void update() {

            if (timeRange != null) {
                title.setText(timeRange.name);

                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                title.setText(timeRange.name);
                endTime.setText(df.format(timeRange.endTime));
                startTime.setText(df.format(timeRange.starTime));
                temperature.setText(timeRange.temperature.toString());
            }
        }

    /*public class HeaterViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView status;
        protected TextView target;
        protected int id;


        public HeaterViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            status = (TextView)  v.findViewById(R.id.status);
            target = (TextView) v.findViewById(R.id.target);
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    mCallback.onHeaterClick(id);
                }
            });
        }
    }*/

    }
}