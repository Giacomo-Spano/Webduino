package com.webduino.fragment.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class SensorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> sensorList;

    OnListener mCallback;
    public interface OnListener {
        void onHeaterClick(int id);
    }
    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_TEMPERATURESENSOR = 1;
    private static final int TYPE_HEATER = 2;

    public SensorAdapter(List<CardInfo> sensorList) {
        this.sensorList = sensorList;
    }

    public void swap(List list){
        if (sensorList != null) {
            sensorList.clear();
            sensorList.addAll(list);
        }
        else {
            sensorList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = sensorList.get(i);

        switch (viewHolder.getItemViewType()) {

            case TYPE_HEATER:
                HeaterViewHolder heaterViewHolder = (HeaterViewHolder) viewHolder;
                HeaterCardInfo hci = (HeaterCardInfo) ci;

                heaterViewHolder.id = hci.actuatorId;
                //heaterViewHolder.heater = hci.heater;
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

        if (sensorList.get(position) instanceof HeaterCardInfo )
            return TYPE_HEATER;
        else if (sensorList.get(position) instanceof TemperatureSensorCardInfo )
            return TYPE_TEMPERATURESENSOR;

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_TEMPERATURESENSOR;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEMPERATURESENSOR) {
            return (RecyclerView.ViewHolder) new TemperatureSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_temperature, parent, false));
        } else if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        }
        return null;
    }

    public class TemperatureSensorViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView temperature;
        protected TextView target;

        public TemperatureSensorViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.titleEditText);
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
            title =  (TextView) v.findViewById(R.id.titleEditText);
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