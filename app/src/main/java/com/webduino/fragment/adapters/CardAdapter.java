package com.webduino.fragment.adapters;

//import android.content.Context;

import android.content.Context;
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

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.HornSensorCardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;
import com.webduino.fragment.cardinfo.TimeRangeCardInfo;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private List<CardInfo> cardInfoList;

    OnListener mCallback;

    @Override
    public void onItemDismiss(int position) {
        cardInfoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cardInfoList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cardInfoList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        //return true;
    }

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

    public void add(CardInfo cardInfo) {
        if (cardInfoList != null) {
            cardInfoList.add(cardInfo);
            notifyDataSetChanged();
        } /*else {
            cardInfoList.add(cardInfo);
        }*/

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

    }

    @Override
    public int getItemViewType(int position) {
        return cardInfoList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = MainActivity.activity.getApplicationContext();

        if (viewType == CardInfo.TYPE_TEMPERATURESENSOR) {
            return (RecyclerView.ViewHolder) new CardAdapter.TemperatureSensorViewHolder(LayoutInflater.from(context).inflate(R.layout.card_temperature, parent, false));
        } else if (viewType == CardInfo.TYPE_DOORSENSOR) {
            return (RecyclerView.ViewHolder) new DoorSensorViewHolder(LayoutInflater.from(context).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_HORNSENSOR) {
            return (RecyclerView.ViewHolder) new HornSensorViewHolder(LayoutInflater.from(context).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_ONEWIRESENSOR) {
            return (RecyclerView.ViewHolder) new OnewireViewHolder(LayoutInflater.from(context).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_SENSOR) {
            return (RecyclerView.ViewHolder) new CardViewHolder(LayoutInflater.from(context).inflate(R.layout.card_door, parent, false));
        } else if (viewType == CardInfo.TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(context).inflate(R.layout.card_heater, parent, false));
        } else if (viewType == CardInfo.TYPE_SCENARIO) {
            return (RecyclerView.ViewHolder) new ScenarioViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_TIMEINTERVAL) {
            return (RecyclerView.ViewHolder) new TimeIntervalViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_TRIGGER) {
            return (RecyclerView.ViewHolder) new TriggerViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_PROGRAMTIMERANGE) {
            return (RecyclerView.ViewHolder) new ProgramTimerangeViewHolder(LayoutInflater.from(context).inflate(R.layout.card_programtimerange, parent, false));
        } else if (viewType == CardInfo.TYPE_PROGRAM) {
            return (RecyclerView.ViewHolder) new ProgramViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_PROGRAMACTION) {
            return (RecyclerView.ViewHolder) new ProgramActionViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_OPTION) {
            return (RecyclerView.ViewHolder) new OptionViewHolder(LayoutInflater.from(context).inflate(R.layout.card_option, parent, false));
        } else if (viewType == CardInfo.TYPE_ACTIONBUTTON) {
            return (RecyclerView.ViewHolder) new ActionButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.card_actionbutton, parent, false));
        } else if (viewType == CardInfo.TYPE_WEBDUINOSYSTEM) {
            return (RecyclerView.ViewHolder) new WebduinoSystemViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_WEBDUINOSYSTEMZONE) {
            return (RecyclerView.ViewHolder) new WebduinoSystemZoneViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        } else if (viewType == CardInfo.TYPE_WEBDUINOSYSTEMACTUATOR) {
            return (RecyclerView.ViewHolder) new WebduinoSystemActuatorViewHolder(LayoutInflater.from(context).inflate(R.layout.card_scenario, parent, false));
        }

        return null;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView image;
        protected TextView label;
        protected ImageView statusImage;
        protected int disabledColor;
        protected int id;

        public CardViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.titleEditText);
            image = (ImageView) v.findViewById(R.id.image);
            label = (TextView) v.findViewById(R.id.label);
            statusImage = (ImageView) v.findViewById(R.id.statusImage);
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
            if (title == null || label == null || image == null)
                return;
            title.setText(ci.name);
            // la label è lo stato (es. temperatua, aperto/chiuso, acceso/spento, etc
            label.setText(getStatusText(ci));

            if (ci.getEnabled()) {
                // l'immagine è il tipo di sensore
                image.setImageDrawable(getIcon(ci));
                image.setColorFilter(new LightingColorFilter(/*Color.RED*/getIconColor(ci), getIconColor(ci)));
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

    public class OptionViewHolder extends CardViewHolder {

        private final TextView valueTextView;
        private final TextView nameTextView;

        public OptionViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.nameTextView);
            valueTextView = (TextView) v.findViewById(R.id.valueTextView);
        }

        public void updateCard(CardInfo ci) {

            if (nameTextView == null || valueTextView == null)
                return;
            OptionCardInfo oci = (OptionCardInfo) ci;
            if (oci != null && oci.value != null) {
                nameTextView.setText(oci.value.getName());
                valueTextView.setText(oci.value.getStringValue());
            }
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

    public class HornSensorViewHolder extends CardViewHolder {

        public HornSensorViewHolder(View v) {
            super(v);
        }

        public Drawable getIcon(CardInfo ci) {

            if (((HornSensorCardInfo) ci).openStatus)
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.opendoor, null);
            else
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.closeddoor, null);
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public String getStatusText(CardInfo ci) {
            if (!((HornSensorCardInfo) ci).openStatus)
                return "Sirena non attiva";
            else
                return "Sirena atttiva";
        }

        public int getStatusColor(CardInfo ci) {
            if (((HornSensorCardInfo) ci).openStatus)
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

    public class WebduinoSystemZoneViewHolder extends ScenarioInfoViewHolder {

        public WebduinoSystemZoneViewHolder(View v) {
            super(v);
        }
    }

    public class WebduinoSystemActuatorViewHolder extends ScenarioInfoViewHolder {

        public WebduinoSystemActuatorViewHolder(View v) {
            super(v);
        }
    }

    public class WebduinoSystemViewHolder extends ScenarioInfoViewHolder {

        public WebduinoSystemViewHolder(View v) {
            super(v);
        }
    }

    public class TimeIntervalViewHolder extends ScenarioInfoViewHolder {

        public TimeIntervalViewHolder(View v) {
            super(v);
        }
    }

    public class TriggerViewHolder extends ScenarioInfoViewHolder {

        public TriggerViewHolder(View v) {
            super(v);
        }
    }

    public class ProgramViewHolder extends ScenarioInfoViewHolder {

        public ProgramViewHolder(View v) {
            super(v);
        }
    }

    public class ProgramTimerangeViewHolder extends ScenarioInfoViewHolder {

        protected TextView startTextView, endTextView, moreTextView;

        public ProgramTimerangeViewHolder(View v) {
            super(v);
            startTextView = (TextView) v.findViewById(R.id.startTextView);
            endTextView = (TextView) v.findViewById(R.id.endTextView);
            moreTextView = (TextView) v.findViewById(R.id.moreTextView);
            moreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

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
            super.updateCard(ci);

            TimeRangeCardInfo tci = (TimeRangeCardInfo) ci;
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            if (tci.startTime != null && startTextView != null)
                startTextView.setText(df.format(tci.startTime));
            if (tci.startTime != null && endTextView != null)
                endTextView.setText(df.format(tci.endTime));
        }
    }

    public class ProgramActionViewHolder extends ScenarioInfoViewHolder {

        public ProgramActionViewHolder(View v) {
            super(v);
        }
    }

    public class ScenarioViewHolder extends ScenarioInfoViewHolder {

        public ScenarioViewHolder(View v) {
            super(v);
        }
    }

    public class ScenarioInfoViewHolder extends CardViewHolder {

        public Drawable getIcon(CardInfo ci) {
            //ScenarioCardInfo hci = (ScenarioCardInfo) ci;
            return ci.imageDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.closeddoor, null);
        }

        public String getStatusText(CardInfo ci) {
            //ScenarioCardInfo hci = (ScenarioCardInfo) ci;
            if (ci.status) {
                return "Attivo";
            } else {
                return "Non attivo";
            }
        }

        public int getStatusColor(CardInfo ci) {

            if (!ci.enabled) {
                return Color.GRAY;
            } else {

                if (ci.status)
                    return Color.GREEN;
                else
                    return Color.RED;
            }
        }

        public int getIconColor(CardInfo ci) {
            return getStatusColor(ci);
        }

        public ScenarioInfoViewHolder(View v) {
            super(v);
            /*targetTextView = (TextView) v.findViewById(R.id.target);
            sensorTextView = (TextView) v.findViewById(R.id.sensor);
            temperatureTextView = (TextView) v.findViewById(R.id.temperature);*/
        }

        public void updateCard(CardInfo ci) {
            super.updateCard(ci);


            /*if (ci.getEnabled()) {
                targetTextView.setTextColor(ci.titleColor);
            } else {
                targetTextView.setTextColor(disabledColor);
            }
            targetTextView.setVisibility(visibility);
            sensorTextView.setVisibility(visibility);
            temperatureTextView.setVisibility(visibility);

            ScenarioCardInfo hci = (ScenarioCardInfo) ci;
            sensorTextView.setText(hci.zone);
            //temperatureTextView.setText(hci.temperature + " °C");*/

        }

    }
}