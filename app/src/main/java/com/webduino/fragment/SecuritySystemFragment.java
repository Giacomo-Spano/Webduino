package com.webduino.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.TimeIntervalCardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemActuatorCardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemCardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemZoneCardInfo;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.Scenarios;
import com.webduino.webduinosystems.SecuritySystem;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystemActuator;
import com.webduino.webduinosystems.WebduinoSystemZone;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.webduino.elements.HeaterActuator.Command_Manual;

public class SecuritySystemFragment extends WebduinoSystemFragment {

    private CardAdapter cardAdapter;
    private TextView targetTextView, zoneTextView, statusTextView;
    Button okButton, cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (savedInstanceState != null) {
        }

        View v = createView(inflater, container, R.layout.fragment_securitysystem);

        targetTextView = (TextView) v.findViewById(R.id.targetTextView);
        zoneTextView = (TextView) v.findViewById(R.id.zoneTextView);
        statusTextView = (TextView) v.findViewById(R.id.statusTextView);

        okButton = (Button) v.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             
                cancelButton.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);
            }
        });
        cancelButton = (Button) v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelButton.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);
            }
        });

        cancelButton.setVisibility(View.INVISIBLE);
        okButton.setVisibility(View.INVISIBLE);

        ImageButton increaseButton = (ImageButton) v.findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
            }
        });
        ImageButton decreaseButton = (ImageButton) v.findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
            }
        });

        refreshData();
        return v;
    }

    public void refreshData() {
    }

    @Override
    public void onClickScenario(CardInfo cardInfo) {

    }

    @Override
    public void onClickZone(CardInfo cardInfo) {

    }

    @Override
    public void onClickActuator(CardInfo cardInfo) {

    }
}
