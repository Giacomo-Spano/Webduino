package com.webduino.fragment.adapters;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> cardInfoList;

    OnListener mCallback;

    public interface OnListener {
        //void onHeaterClick(int id);
        void onClick(int position, CardInfo cardInfo);
    }

    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_TEMPERATURESENSOR = 1;
    private static final int TYPE_HEATER = 2;
    private static final int TYPE_ACTIONBUTTON = 3;

    public CardAdapter(List<CardInfo> cardInfoList) {
        this.cardInfoList = cardInfoList;
    }

    public void swap(List list) {
        if (cardInfoList != null) {
            cardInfoList.clear();
            cardInfoList.addAll(list);
        } else {
            cardInfoList = list;
        }
        notifyDataSetChanged();
    }

    public void setCardInfo(int position, CardInfo card) {
        if (position >= 0 && position < cardInfoList.size() && card != null)
            cardInfoList.set(position, card);
    }


    @Override
    public int getItemCount() {
        return cardInfoList.size();
    }

    public CardInfo getCardInfoFromId(int id) {
        for (CardInfo card : cardInfoList) {
            if (card.id == id)
                return card;
        }
        return null;
    }

    public int getPositionFromId(int id) {
        int i = 0;
        for (CardInfo card : cardInfoList) {
            if (card.id == id)
                return i;
            i++;
        }
        return -1;
    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

        if (holder instanceof ActionButtonViewHolder) {
            super.onBindViewHolder(holder,position, payloads);
        } else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = cardInfoList.get(i);

        if (viewHolder instanceof ActionButtonViewHolder) {

            ActionButtonViewHolder actionButtonViewHolder = (ActionButtonViewHolder) viewHolder;
            ActionButtonCardInfo abci = (ActionButtonCardInfo) ci;
            actionButtonViewHolder.id = abci.id;
            actionButtonViewHolder.updateCard(abci);

        } else if (viewHolder instanceof HeaterViewHolder) {

            HeaterViewHolder heaterViewHolder = (HeaterViewHolder) viewHolder;
            HeaterCardInfo hci = (HeaterCardInfo) ci;
            ci.label = hci.status;
            if (hci.releStatus)
                ci.labelBackgroundColor = Color.GREEN;
            else
                ci.labelBackgroundColor = Color.RED;
            heaterViewHolder.id = hci.actuatorId;

            if (hci.hideTarget) {
                heaterViewHolder.setVisibility(View.GONE);
            } else {
                heaterViewHolder.setVisibility(View.VISIBLE);
                heaterViewHolder.sensorTextView.setText(hci.sensorName);
                heaterViewHolder.temperatureTextView.setText(""+hci.sensorTemperature+"°C");
                heaterViewHolder.targetTextView.setText(""+hci.target+"°C");
            }

            heaterViewHolder.updateCard(ci);

        } else if (viewHolder instanceof TemperatureSensorViewHolder) {

            TemperatureSensorViewHolder temperatureSensorViewHolder = (TemperatureSensorViewHolder) viewHolder;
            TemperatureSensorCardInfo tci = (TemperatureSensorCardInfo) ci;

            /*int temperatureColor = CardInfo.getTemperatureColor(tci.temperature);
            ci.setColor(temperatureColor);*/

            if (tci.online) {
                tci.label = "Online";
            } else {
                tci.label = "Offline";
            }
            temperatureSensorViewHolder.temperature = tci.temperature;
            temperatureSensorViewHolder.id = tci.id;

            temperatureSensorViewHolder.updateCard(ci);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (cardInfoList.get(position) instanceof HeaterCardInfo)
            return TYPE_HEATER;
        else if (cardInfoList.get(position) instanceof TemperatureSensorCardInfo)
            return TYPE_TEMPERATURESENSOR;
        else if (cardInfoList.get(position) instanceof ActionButtonCardInfo)
            return TYPE_ACTIONBUTTON;

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
        } else if (viewType == TYPE_ACTIONBUTTON) {
            return (RecyclerView.ViewHolder) new ActionButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_actionbutton, parent, false));
        }
        return null;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView image;
        protected TextView label;
        protected int disabledColor;
        protected int id;

        public CardViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.titleEditText);
            image = (ImageView) v.findViewById(R.id.image);
            label = (TextView) v.findViewById(R.id.label);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // item clicked
                    int position = getAdapterPosition();
                    CardInfo card = cardInfoList.get(position);
                    mCallback.onClick(position, card);
                }
            });
        }

        public void updateCard(CardInfo ci) {

            title.setText(ci.name);
            label.setText(ci.label);

            if (ci.getEnabled()) {
                image.setImageDrawable(ci.imageDrawable);
                image.setColorFilter(new LightingColorFilter(ci.imageColor, ci.imageColor));
                title.setTextColor(ci.titleColor);
                label.setBackgroundColor(ci.labelBackgroundColor);
                label.setTextColor(ci.labelColor);
            } else {
                image.setImageDrawable(ci.imageDrawable);
                disabledColor = Color.parseColor("#dddddd");
                image.setColorFilter(disabledColor, PorterDuff.Mode.SRC_IN);
                title.setTextColor(disabledColor);
                label.setBackgroundColor(disabledColor);
                label.setTextColor(ci.labelColor);
            }
        }
    }

    public class ActionButtonViewHolder extends CardViewHolder {

        public ActionButtonViewHolder(View v) {
            super(v);
        }

        public void updateCard(ActionButtonCardInfo ci) {
            super.updateCard(ci);
        }
    }

    public class TemperatureSensorViewHolder extends CardViewHolder {
        protected TextView temperatureTextView;
        protected double temperature;

        public TemperatureSensorViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.titleEditText);
            temperatureTextView = (TextView) v.findViewById(R.id.temperature);
        }

        public void updateCard(CardInfo ci) {
            super.updateCard(ci);
            if (ci.getEnabled()) {
                temperatureTextView.setTextColor(ci.titleColor);
            } else {
                temperatureTextView.setTextColor(disabledColor);
            }
            temperatureTextView.setText(""+temperature+"°C");
        }
    }

    public class HeaterViewHolder extends CardViewHolder {
        protected TextView targetTextView;
        protected int actuatorId;
        protected double target;
        protected int visibility;
        protected TextView sensorTextView;
        protected TextView temperatureTextView;


        public HeaterViewHolder(View v) {
            super(v);
            targetTextView = (TextView) v.findViewById(R.id.target);
            sensorTextView = (TextView) v.findViewById(R.id.sensor);
            temperatureTextView = (TextView) v.findViewById(R.id.temperature);
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    mCallback.onHeaterClick(id);
                }
            });*/
        }

        public void updateCard(CardInfo ci) {
            super.updateCard(ci);
            if (ci.getEnabled()) {
                targetTextView.setTextColor(ci.titleColor);
            } else {
                targetTextView.setTextColor(disabledColor);
            }
            targetTextView.setVisibility(visibility);
            sensorTextView.setVisibility(visibility);
            temperatureTextView.setVisibility(visibility);

        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }
    }
}