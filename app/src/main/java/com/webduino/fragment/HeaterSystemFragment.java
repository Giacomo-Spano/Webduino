package com.webduino.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;

public class HeaterSystemFragment extends WebduinoSystemFragment {

    private CardAdapter cardAdapter;
    private TextView targetTextView, zoneTextView, statusTextView;
    Button okButton, cancelButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (savedInstanceState != null) {
        }

        View v = createView(inflater, container, R.layout.fragment_heatersystem);

        refreshData();
        return v;
    }

    public void refreshData() {
    }

    @Override
    public void onClickScenario(CardInfo cardInfo) {
        super.onClickScenario(cardInfo);
    }

    @Override
    public void onClickZone(CardInfo cardInfo) {
        super.onClickZone(cardInfo);
    }

    @Override
    public void onClickActuator(CardInfo cardInfo) {

        HeaterCardInfo heaterCerdInfo = (HeaterCardInfo) cardInfo;
        Bundle bundle = new Bundle();
        bundle.putString("id", "" + heaterCerdInfo.id);
        bundle.putString("shieldid", "" + heaterCerdInfo.shieldid);

        HeaterFragment heaterFragment = new HeaterFragment();

        heaterFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment)heaterFragment);
        ft.addToBackStack(null);
        ft.commit();

    }
}
