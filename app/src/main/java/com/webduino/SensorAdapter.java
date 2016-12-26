package com.webduino;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class SensorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> contactList;

    OnListener mCallback;
    public interface OnListener {
        void onHeaterClick(int id);
        //void onEnablePanelRefreshButtonRequest();
    }
    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_TEMPERATURESENSOR = 1;
    private static final int TYPE_HEATER = 2;

    public SensorAdapter(List<CardInfo> contactList) {
        this.contactList = contactList;
    }

    public void swap(List list){
        if (contactList != null) {
            contactList.clear();
            contactList.addAll(list);
        }
        else {
            contactList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = contactList.get(i);

        switch (viewHolder.getItemViewType()) {

            case TYPE_HEATER:
                HeaterViewHolder heaterViewHolder = (HeaterViewHolder) viewHolder;
                HeaterCardInfo hci = (HeaterCardInfo) ci;

                heaterViewHolder.id = hci.actuatorId;
                heaterViewHolder.title.setText(hci.name);
                heaterViewHolder.target.setText(""+hci.target+"°");
                heaterViewHolder.status.setText(hci.status);
                if (hci.releStatus)
                    heaterViewHolder.status.setBackgroundColor(Color.GREEN);
                else
                    heaterViewHolder.status.setBackgroundColor(Color.RED);

                break;

            case TYPE_TEMPERATURESENSOR:
                TemperatureSensorViewHolder temperatureSensorViewHolder = (TemperatureSensorViewHolder) viewHolder;
                TemperatureSensorCardInfo tci = (TemperatureSensorCardInfo) ci;

                temperatureSensorViewHolder.title.setText(tci.name);
                temperatureSensorViewHolder.temperature.setText(""+tci.temperature+"°");
                temperatureSensorViewHolder.target.setText(""+tci.target+"°");

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (contactList.get(position) instanceof HeaterCardInfo )
            return TYPE_HEATER;
        else if (contactList.get(position) instanceof TemperatureSensorCardInfo )
            return TYPE_TEMPERATURESENSOR;

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_TEMPERATURESENSOR;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEMPERATURESENSOR) {
            return (RecyclerView.ViewHolder) new TemperatureSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.temperaturecard_layout, parent, false));
        }

        if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.heatercard_layout, parent, false));
        }
        return null;
    }

    public class TemperatureSensorViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView temperature;
        protected TextView target;

        public TemperatureSensorViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            temperature = (TextView)  v.findViewById(R.id.temperature);
            target = (TextView) v.findViewById(R.id.target);
        }
    }

    public class HeaterViewHolder extends RecyclerView.ViewHolder {
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
    }
}