package com.webduino.fragment.adapters;

//import android.content.Context;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
//import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.OnewireCardInfo;
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


    private android.app.Fragment context;

    public CardAdapter(android.app.Fragment context, List<CardInfo> cardInfoList) {
        this.cardInfoList = cardInfoList;
        this.context = context;
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = cardInfoList.get(i);

        ((CardViewHolder) viewHolder).updateCard(ci);


        /*if (viewHolder instanceof ActionButtonViewHolder) {

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



            if (tci.online) {
                tci.label = "Online";
            } else {
                tci.label = "Offline";
            }
            temperatureSensorViewHolder.temperature = tci.temperature;
            temperatureSensorViewHolder.id = tci.id;

            temperatureSensorViewHolder.updateCard(ci);
        }*/
    }

    @Override
    public int getItemViewType(int position) {

        return cardInfoList.get(position).getSensorType();

        /*if (cardInfoList.get(position) instanceof HeaterCardInfo)
            return TYPE_HEATER;
        else if (cardInfoList.get(position) instanceof TemperatureSensorCardInfo)
            return TYPE_TEMPERATURESENSOR;
        else if (cardInfoList.get(position) instanceof DoorSensorCardInfo)
            return TYPE_DOORSENSOR;
        else if (cardInfoList.get(position) instanceof ActionButtonCardInfo)
            return TYPE_ACTIONBUTTON;*/

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_TEMPERATURESENSOR;
        //return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //CardInfoFactory factory = new CardInfoFactory();
        //RecyclerView.ViewHolder viewHolder = factory.createCardInfo(parent, viewType);

        if (viewType == CardInfo.TYPE_TEMPERATURESENSOR) {
            return (RecyclerView.ViewHolder) new CardAdapter.TemperatureSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_temperature, parent, false));
        } else if (viewType == CardInfo.TYPE_DOORSENSOR) {
            return (RecyclerView.ViewHolder) new DoorSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_ONEWIRESENSOR) {
            return (RecyclerView.ViewHolder) new OnewireViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_SENSOR) {
            return (RecyclerView.ViewHolder) new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        }

        /*if (viewType == CardInfo.TYPE_TEMPERATURESENSOR) {
            return (RecyclerView.ViewHolder) new TemperatureSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_temperature, parent, false));
        } else if (viewType == TYPE_DOORSENSOR) {
            return (RecyclerView.ViewHolder) new DoorSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
        } else if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        } else if (viewType == TYPE_ACTIONBUTTON) {
            return (RecyclerView.ViewHolder) new ActionButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_actionbutton, parent, false));
        }*/
        return null;
    }

    /*public class CardInfoFactory {

        public RecyclerView.ViewHolder createCardInfo(ViewGroup parent, int type) {

            if (type == CardInfo.TYPE_TEMPERATURESENSOR) {
                return (RecyclerView.ViewHolder) new CardAdapter.TemperatureSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_temperature, parent, false));
            } else if (type == CardInfo.TYPE_DOORSENSOR) {
                return (RecyclerView.ViewHolder) new DoorSensorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
            } else if (type == CardInfo.TYPE_ONEWIRESENSOR) {
                return (RecyclerView.ViewHolder) new OnewireViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
            } else if (type == CardInfo.TYPE_SENSOR) {
                return (RecyclerView.ViewHolder) new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_door, parent, false));
            }

            return null;
        }
    }*/

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

            // il title è il nome del sensore
            title.setText(ci.name);
            // la label è lo stato (es. temperatua, aperto/chiuso, acceso/spento, etc
            label.setText(getStatusText(ci));

            if (ci.getEnabled()) {
                // l'immagine è il tipo di sensore
                image.setImageDrawable(getIcon(ci));
                image.setColorFilter(new LightingColorFilter(getIconColor(ci), getIconColor(ci)));
                title.setTextColor(getTitleColor(ci));
                // il background di label cambia in base allo stao (es. temperatua, aperto/chiuso, acceso/spento, etc
                label.setBackgroundColor(getStatusColor(ci));
                //label.setTextColor(ci.labelColor);
                label.setTextColor(Color.WHITE);
            } else {
                image.setImageDrawable(getIcon(ci));
                disabledColor = Color.parseColor("#dddddd");
                image.setColorFilter(disabledColor, PorterDuff.Mode.SRC_IN);
                title.setTextColor(disabledColor);
                label.setBackgroundColor(disabledColor);
                //label.setTextColor(ci.labelColor);
                label.setTextColor(Color.WHITE);
            }

            if (!ci.online) {
                label.setBackgroundColor(Color.GRAY);
                label.setTextColor(Color.WHITE);
                ci.label = "OFFLINE";
            }
        }

        public String getStatusText(CardInfo ci) {
            return ci.label;
        }

        public Drawable getIcon(CardInfo ci) {
            return ci.imageDrawable;
        }

        public int getTitleColor(CardInfo ci) {
            return Color.GRAY;
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public int getStatusColor(CardInfo ci) {
            return ci.labelBackgroundColor;
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
        //protected double temperature;

        public TemperatureSensorViewHolder(View v) {
            super(v);
            //title = (TextView) v.findViewById(R.id.titleEditText);
        }

        public String getStatusText(CardInfo ci) {
            double temperature = ((TemperatureSensorCardInfo) ci).temperature;
            return "" + temperature + "°C";
        }

        public Drawable getIcon(CardInfo ci) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.temperature, null);
            //return ResourcesCompat.getDrawable(context.getResources(), R.drawable.temperature, null);
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public int getStatusColor(CardInfo ci) {
            int temperatureColor = CardInfo.getTemperatureColor(((TemperatureSensorCardInfo) ci).temperature);
            return temperatureColor;
        }
    }

    public class DoorSensorViewHolder extends CardViewHolder {

        public DoorSensorViewHolder(View v) {
            super(v);
        }

        public Drawable getIcon(CardInfo ci) {

            if (((DoorSensorCardInfo) ci).openStatus)
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.opendoor, null);
            else
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.closeddoor, null);
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public String getStatusText(CardInfo ci) {
            if (!((DoorSensorCardInfo) ci).openStatus)
                return "Chiusa";
            else
                return "Aperta";
        }

        public int getStatusColor(CardInfo ci) {
            if (((DoorSensorCardInfo) ci).openStatus)
                return Color.RED;
            else
                return Color.GREEN;
        }
    }

    public class OnewireViewHolder extends CardViewHolder {

        public OnewireViewHolder(View v) {
            super(v);
        }

        public Drawable getIcon(CardInfo ci) {

            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.onewire, null);
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public String getStatusText(CardInfo ci) {
            return " ";
        }

        public int getStatusColor(CardInfo ci) {
            return Color.RED;
        }
    }

    public class HeaterViewHolder extends CardViewHolder {
        protected TextView targetTextView;
        protected int actuatorId;
        protected double target;
        protected int visibility;
        protected TextView sensorTextView;
        protected TextView temperatureTextView;

        public Drawable getIcon(CardInfo ci) {
            HeaterCardInfo hci = (HeaterCardInfo) ci;
            if (hci.status.equals("keeptemperature")) {
                return ci.imageDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.auto, null);
            }

            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.heater, null);
            //return ResourcesCompat.getDrawable(context.getResources(), R.drawable.temperature, null);
        }

        public String getStatusText(CardInfo ci) {
            HeaterCardInfo hci = (HeaterCardInfo) ci;
            if (hci.status.equals("keeptemperature")) {
                return "keep " + hci.target + "°C";
            } else if (hci.status.equals("manual")) {
                return "manual " + hci.target + "°C";
            } else if (hci.status.equals("manualoff")) {
                return "manualoff ";
            }
            return hci.status;
        }

        public int getStatusColor(CardInfo ci) {
            if (((HeaterCardInfo) ci).releStatus)
                return Color.GREEN;
            else
                return Color.RED;
        }


        /*if (ci.status.equals("keeptemperature")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.auto, null);
            ci.target = heater.getTarget();
            ci.sensorName = heater.getSensorIdName();
            ci.sensorTemperature = heater.getRemoteTemperature();
        } else if (ci.status.equals("idle")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        } else if (ci.status.equals("manualoff")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            ci.hideTarget = true;
        } else if (ci.status.equals("manual")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.power, null);
            ci.target = heater.getTarget();
            ci.sensorName = heater.getSensorIdName();
            ci.sensorTemperature = heater.getRemoteTemperature();
        } else if (ci.status.equals("idle")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        } else {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        }*/



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

            HeaterCardInfo hci = (HeaterCardInfo) ci;
            sensorTextView.setText(hci.zone);
            temperatureTextView.setText(hci.temperature + " °C");

        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }
    }
}