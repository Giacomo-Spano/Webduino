package com.webduino.fragment;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DateOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.MultiChoiceOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.ScenarioTimeInterval;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeIntervalFragment extends Fragment {

    ScenarioTimeInterval timeInterval = new ScenarioTimeInterval();
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_Enabled, optionCard_StartDate, optionCard_EndDate, optionCard_daysofweek;

    private OnTimeIntervalFragmentInteractionListener mListener;

    public TimeIntervalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeinterval, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(true);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.setListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton,getResources().getColor(R.color.colorPrimary),true,getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeInterval.name = optionCard_Name.value.getStringValue();
                timeInterval.description = optionCard_Description.value.getStringValue();
                Object val = optionCard_Enabled.value;
                timeInterval.enabled = optionCard_Enabled.value.getBoolValue();

                MultiChoiceOptionCardValue multichioce = (MultiChoiceOptionCardValue)optionCard_daysofweek.value;
                timeInterval.monday = multichioce.getValue(0);
                timeInterval.tuesday = multichioce.getValue(1);
                timeInterval.wednesday = multichioce.getValue(2);
                timeInterval.thursday = multichioce.getValue(3);
                timeInterval.friday = multichioce.getValue(4);
                timeInterval.saturday = multichioce.getValue(5);
                timeInterval.sunday = multichioce.getValue(6);

                if (mListener != null) {
                    mListener.onSaveTimeInterval(timeInterval);

                }
                getActivity().getFragmentManager().popBackStack();
            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton,getResources().getColor(R.color.colorPrimary),false,getResources().getDrawable(R.drawable.uncheck));
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnTimeIntervalFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTimeIntervalFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveTimeInterval(ScenarioTimeInterval timeInterval);
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue(getString(R.string.status),timeInterval.enabled,"Abilitato","Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue(getString(R.string.name),timeInterval.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue(getString(R.string.description),timeInterval.description);
        result.add(optionCard_Description);

        optionCard_StartDate = new OptionCardInfo();
        optionCard_StartDate.value = new DateOptionCardValue(getString(R.string.startdate),timeInterval.startDateTime);
        result.add(optionCard_StartDate);

        optionCard_EndDate = new OptionCardInfo();
        optionCard_EndDate.value = new DateOptionCardValue(getString(R.string.enddate),timeInterval.endDateTime);
        result.add(optionCard_EndDate);

        boolean[] itemValues = {
                timeInterval.monday, timeInterval.tuesday, timeInterval.wednesday, timeInterval.thursday, timeInterval.friday, timeInterval.saturday, timeInterval.sunday
        };
        optionCard_daysofweek = new OptionCardInfo();
        optionCard_daysofweek.value = new MultiChoiceOptionCardValue(getString(R.string.dayofweek),getResources().getStringArray(R.array.dayshortlist),itemValues);
        result.add(optionCard_daysofweek);

        return result;
    }


}
